package juuxel.resin.api.util;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Text;

import java.util.function.Function;

/**
 * Additional methods for working with codecs.
 */
public final class Codecs {
	public static final Codec<Text> TEXT = new Codec<Text>() {
		@Override
		public <T> DataResult<Pair<Text, T>> decode(DynamicOps<T> ops, T input) {
			try {
				JsonElement json = Dynamic.convert(ops, JsonOps.INSTANCE, input);
				return DataResult.success(Pair.of(Text.Serializer.fromJson(json), ops.empty()));
			} catch (Exception e) {
				return DataResult.error(e.getMessage());
			}
		}

		@Override
		public <T> DataResult<T> encode(Text input, DynamicOps<T> ops, T prefix) {
			return ops.mergeToPrimitive(prefix, Dynamic.convert(JsonOps.INSTANCE, ops, Text.Serializer.toJsonTree(input)));
		}

		@Override
		public String toString() {
			return "Text";
		}
	};

	private Codecs() {}

	/**
	 * Transforms a codec for {@code A} with a {@code (A) -> Codec<B>} function.
	 *
	 * Unlike {@link Codec#dispatch(Function, Function)},
	 * this method does not create a separate field for the value codec.
	 * Instead, the value is read from the main input.
	 *
	 * @param codec         The original codec.
	 * @param origin        A function that extracts A from B.
	 * @param codecProvider A function that provides a codec for B from an instance of A.
	 * @param <A> The original codec type.
	 * @param <B> The transformed codec type.
	 * @return The transformed codec.
	 */
	public static <A, B> Codec<B> flatDispatch(Codec<A> codec, Function<? super B, ? extends A> origin, Function<? super A, ? extends Codec<B>> codecProvider) {
		Codec<A> typeKey = RecordCodecBuilder.create(instance -> instance.group(codec.fieldOf("type").forGetter(x -> x)).apply(instance, x -> x));
		return merge(typeKey, origin, codecProvider);
	}

	/**
	 * Merged a codec for {@code A} with a codec provided by a {@code (A) -> Codec<B>} function.
	 *
	 * @param codec         The original codec.
	 * @param origin        A function that extracts A from B.
	 * @param codecProvider A function that provides a codec for B from an instance of A.
	 * @param <A> The original codec type.
	 * @param <B> The second codec type.
	 * @return The merged codec that will read both values from the same input.
	 */
	public static <A, B> Codec<B> merge(Codec<A> codec, Function<? super B, ? extends A> origin, Function<? super A, ? extends Codec<B>> codecProvider) {
		return new Codec<B>() {
			@Override
			public <T> DataResult<Pair<B, T>> decode(DynamicOps<T> ops, T input) {
				return codec.decode(ops, input).flatMap(result -> codecProvider.apply(result.getFirst()).decode(ops, input).map(Codecs::removeCovariance));
			}

			@Override
			public <T> DataResult<T> encode(B input, DynamicOps<T> ops, T prefix) {
				A a = origin.apply(input);
				return codec.encode(a, ops, prefix).flatMap(next -> codecProvider.apply(a).encode(input, ops, next));
			}

			@Override
			public String toString() {
				return codec.toString() + "[merged]";
			}
		};
	}

	/**
	 * Removes the covariance of a pair by casting.
	 *
	 * @param pair The covariant pair.
	 * @param <F> The first data type in the pair.
	 * @param <S> The second data type in the pair.
	 * @return The specified pair instance.
	 */
	@SuppressWarnings("unchecked")
	private static <F, S> Pair<F, S> removeCovariance(Pair<? extends F, ? extends S> pair) {
		return (Pair<F, S>) pair;
	}
}

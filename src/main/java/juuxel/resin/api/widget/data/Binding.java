package juuxel.resin.api.widget.data;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class Binding<A> {
	private static final String KEY_PREFIX = "$$";

	public static <A> Codec<Binding<A>> codec(Codec<A> valueCodec) {
		return new Codec<Binding<A>>() {
			@Override
			public <T> DataResult<Pair<Binding<A>, T>> decode(DynamicOps<T> ops, T input) {
				Optional<String> stringValue = ops.getStringValue(input).get().left();

				if (stringValue.isPresent()) {
					String str = stringValue.get();
					if (str.startsWith(KEY_PREFIX) && str.length() > KEY_PREFIX.length()) {
						String key = str.substring(KEY_PREFIX.length());
						return DataResult.success(Pair.of(new Bound<>(key), ops.empty()));
					}
				}

				return valueCodec.decode(ops, input).map(pair -> pair.mapFirst(Constant::new));
			}

			@Override
			public <T> DataResult<T> encode(Binding<A> input, DynamicOps<T> ops, T prefix) {
				if (input instanceof Bound<?>) {
					return ops.mergeToPrimitive(prefix, ops.createString(KEY_PREFIX + ((Bound<A>) input).getKey()));
				}

				return valueCodec.encode(((Constant<A>) input).getValue(), ops, prefix);
			}

			@Override
			public String toString() {
				return "Binding[" + valueCodec + "]";
			}
		};
	}

	private Binding() {}

	public abstract A get(Bindings context);

	public static final class Bound<A> extends Binding<A> {
		private final String key;

		public Bound(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}

		@Override
		public A get(Bindings context) {
			return context.<A>get(key).orElseThrow(() -> new NoSuchElementException("No binding for key '" + key + "'!"));
		}
	}

	public static final class Constant<A> extends Binding<A> {
		private final A value;

		public Constant(A value) {
			this.value = value;
		}

		public A getValue() {
			return value;
		}

		@Override
		public A get(Bindings context) {
			return value;
		}
	}
}

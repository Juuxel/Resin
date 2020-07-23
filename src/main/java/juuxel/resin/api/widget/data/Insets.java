package juuxel.resin.api.widget.data;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.Optional;
import java.util.stream.IntStream;

public final class Insets {
	public static final Codec<Insets> CODEC = new Codec<Insets>() {
		@Override
		public <T> DataResult<Pair<Insets, T>> decode(DynamicOps<T> ops, T input) {
			Optional<Integer> intValue = ops.getNumberValue(input).get().left().map(Number::intValue);

			if (intValue.isPresent()) {
				int all = intValue.get();
				return DataResult.success(Pair.of(new Insets(all, all, all, all), ops.empty()));
			}

			return ops.getIntStream(input).map(IntStream::toArray)
				.flatMap(ints -> {
					if (ints.length != 4) {
						return DataResult.error("Insets list must have exactly 4 elements, found " + ints.length);
					}

					return DataResult.success(Pair.of(new Insets(ints[0], ints[1], ints[2], ints[3]), ops.empty()));
				});
		}

		@Override
		public <T> DataResult<T> encode(Insets input, DynamicOps<T> ops, T prefix) {
			return ops.mergeToPrimitive(
				prefix,
				ops.createIntList(IntStream.of(input.getTop(), input.getLeft(), input.getBottom(), input.getRight()))
			);
		}

		@Override
		public String toString() {
			return "Insets";
		}
	};

	private final int top;
	private final int left;
	private final int bottom;
	private final int right;

	public Insets(int top, int left, int bottom, int right) {
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}

	public int getTop() {
		return top;
	}

	public int getLeft() {
		return left;
	}

	public int getBottom() {
		return bottom;
	}

	public int getRight() {
		return right;
	}
}

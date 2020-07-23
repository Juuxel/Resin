package juuxel.resin.api.widget.data;

import java.util.Map;
import java.util.Optional;

public final class Bindings {
	private final Map<String, Object> values;

	public Bindings(Map<String, Object> values) {
		this.values = values;
	}

	@SuppressWarnings("unchecked")
	public <T> Optional<T> get(String key) {
		return Optional.ofNullable((T) values.get(key));
	}
}

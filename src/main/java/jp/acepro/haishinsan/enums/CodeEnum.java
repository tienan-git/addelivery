package jp.acepro.haishinsan.enums;

import java.util.stream.Stream;

/**
 * 列挙クラスのインタフェース。
 *
 */
public interface CodeEnum<T> {

	/**
	 * 名称取得メソッド
	 *
	 * @return 名称
	 */
	public String getLabel();

	/**
	 * 値取得メソッド
	 *
	 * @return 値
	 */
	public T getValue();

	static <T, E extends Enum<?> & CodeEnum<T>> E of(Class<E> clazz, T value) {
		if (value == null) {
			return null;
		}
		return Stream.of(clazz.getEnumConstants()).filter(e -> e.getValue().equals(value)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException(value.toString()));
	}
}

package jp.acepro.haishinsan.converter;

import java.util.Collections;
import java.util.Set;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.util.Assert;

import jp.acepro.haishinsan.annotation.Trim;

public class Trimmer implements ConditionalGenericConverter {

	private static final String TRIM_REGEX = "^[\\s　]+|[\\s　]+$";

	private static final String EMPTY_STRING = "";

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return targetType.hasAnnotation(Trim.class);
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, String.class));
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		Assert.isInstanceOf(String.class, source, "source [" + source + "] is not of class " + String.class.getName());
		return ((String) source).replaceAll(TRIM_REGEX, EMPTY_STRING);
	}
}

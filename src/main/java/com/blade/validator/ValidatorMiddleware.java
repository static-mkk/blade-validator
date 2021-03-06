package com.blade.validator;

import com.blade.mvc.hook.Signature;
import com.blade.mvc.hook.WebHook;
import com.blade.validator.annotation.*;
import com.blade.validator.exception.ValidateException;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 参数校验中间件
 * <p>
 * Created by biezhi on 10/07/2017.
 */
public class ValidatorMiddleware implements WebHook {

    public boolean before(Signature signature) {
        Object[] args   = signature.getParameters();
        Method   method = signature.getAction();

        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (null != parameter.getAnnotation(Valid.class)) {
                try {
                    validate(args[i]);
                } catch (ValidateException e) {
                    throw e;
                } catch (Exception e) {
                    throw new ValidateException(e.getMessage());
                }
            }
        }
        return true;
    }

    private void validate(@NonNull Object object) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {

            field.setAccessible(true);
            Object fieldValue = field.get(object);

            NotNull notNull = field.getAnnotation(NotNull.class);
            if (null != notNull) {
                validateNotNull(notNull.message(), fieldValue);
            }
            NotEmpty notEmpty = field.getAnnotation(NotEmpty.class);
            if (null != notEmpty) {
                validateNotEmpty(notEmpty.message(), fieldValue);
            }
            Length length = field.getAnnotation(Length.class);
            if (null != length) {
                validateLength(length, fieldValue);
            }
            Max max = field.getAnnotation(Max.class);
            if (null != max) {
                validateMax(max, fieldValue);
            }
            Min min = field.getAnnotation(Min.class);
            if (null != min) {
                validateMin(min, fieldValue);
            }
            Email email = field.getAnnotation(Email.class);
            if (null != email) {
                validateEmail(email, fieldValue);
            }
            Url url = field.getAnnotation(Url.class);
            if (null != url) {
                validateUrl(url, fieldValue);
            }
        }
    }

    private void validateUrl(Url url, Object fieldValue) {
        if (null != fieldValue) {
            String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
            if (!Pattern.matches(regex, fieldValue.toString())) {
                throw new ValidateException(url.message());
            }
        }
    }

    public void validateNotNull(String msg, Object val) {
        if (null == val) {
            throw new ValidateException(msg);
        }
    }

    public void validateNotEmpty(String msg, Object val) {
        if (null == val) {
            throw new ValidateException(msg);
        }
        if (val instanceof String && val.toString().isEmpty()) {
            throw new ValidateException(msg);
        }
    }

    public void validateLength(Length length, Object val) {
        if (null != val) {
            if (val.toString().length() > length.max()) {
                String msg = String.format(length.message(), length.max());
                throw new ValidateException(msg);
            }
            if (val.toString().length() < length.min()) {
                String msg = String.format(length.message(), length.min());
                throw new ValidateException(msg);
            }
        }
    }

    public void validateMax(Max max, Object val) {
        if (null != max && null != val) {
            Double d = Double.valueOf(val.toString());
            if (d > max.value()) {
                String msg = String.format(max.message(), max.value());
                throw new ValidateException(msg);
            }
        }
    }

    public void validateMin(Min min, Object val) {
        if (null != min && null != val) {
            Double d = Double.valueOf(val.toString());
            if (d < min.value()) {
                String msg = String.format(min.message(), min.value());
                throw new ValidateException(msg);
            }
        }
    }

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public void validateEmail(Email email, Object val) {
        if (null != val) {
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(val.toString());
            if (!matcher.find()) {
                throw new ValidateException(email.message());
            }
        }
    }

}

package utilities;

public final class CastUtilities {

    @SuppressWarnings("unchecked")
    public static <T extends Number> T convertToType(double value, Class<?> targetClass) {
        if (value == Double.NEGATIVE_INFINITY) {
            return switch (targetClass.getSimpleName().toLowerCase()) {
                case "double" -> (T) Double.valueOf(value);
                case "float" -> (T) Float.valueOf((float) value);
                case "integer", "int" -> (T) Integer.valueOf(Integer.MIN_VALUE);
                case "long" -> (T) Long.valueOf(Long.MIN_VALUE);
                case "short" -> (T) Short.valueOf(Short.MIN_VALUE);
                case "byte" -> (T) Byte.valueOf(Byte.MIN_VALUE);
                default -> throw new IllegalArgumentException("Numeric type not supported: " + targetClass.getName());
            };
        } else if (value == Double.POSITIVE_INFINITY) {
            return switch (targetClass.getSimpleName().toLowerCase()) {
                case "double" -> (T) Double.valueOf(value);
                case "float" -> (T) Float.valueOf((float) value);
                case "integer", "int" -> (T) Integer.valueOf(Integer.MAX_VALUE);
                case "long" -> (T) Long.valueOf(Long.MAX_VALUE);
                case "short" -> (T) Short.valueOf(Short.MAX_VALUE);
                case "byte" -> (T) Byte.valueOf(Byte.MAX_VALUE);
                default -> throw new IllegalArgumentException("Numeric type not supported: " + targetClass.getName());
            };
        } else {
            return switch (targetClass.getSimpleName().toLowerCase()) {
                case "double" -> (T) Double.valueOf(value);
                case "float" -> (T) Float.valueOf((float) value);
                case "integer", "int" -> (T) Integer.valueOf((int) Math.round(value));
                case "long" -> (T) Long.valueOf(Math.round(value));
                case "short" -> (T) Short.valueOf((short) Math.round(value));
                case "byte" -> (T) Byte.valueOf((byte) Math.round(value));
                default -> throw new IllegalArgumentException("Numeric type not supported: " + targetClass.getName());
            };
        }
    }

}

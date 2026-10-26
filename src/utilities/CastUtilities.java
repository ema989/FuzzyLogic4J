package utilities;

public final class CastUtilities {

    @SuppressWarnings("unchecked")
    public static <T extends Number> T convertToType(double value, Class<?> targetClass) {
        return switch (targetClass.getSimpleName()) {
            case "Double" -> (T) Double.valueOf(value);
            case "Float" -> (T) Float.valueOf((float) value);
            case "Integer" -> (T) Integer.valueOf((int) Math.round(value));
            case "Long" -> (T) Long.valueOf(Math.round(value));
            case "Short" -> (T) Short.valueOf((short) Math.round(value));
            case "Byte" -> (T) Byte.valueOf((byte) Math.round(value));
            default -> throw new IllegalArgumentException("Numeric type not supported: " + targetClass.getName());
        };
    }

}

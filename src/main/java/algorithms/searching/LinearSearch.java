package algorithms.searching;

public class LinearSearch {

    public static <T> int search(
            T[] array,
            T target) {

        if (array == null) {
            throw new IllegalArgumentException(
                    "Array cannot be null"
            );
        }

        for (int i = 0; i < array.length; i++) {

            if (target == null
                    ? array[i] == null
                    : target.equals(array[i])) {

                return i;
            }
        }

        return -1;
    }
}
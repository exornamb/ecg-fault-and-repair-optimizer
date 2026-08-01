public class LinearSearch {

    // Checks each number one at a time
    public static int search(int[] arr, int target) {

        for (int i = 0; i < arr.length; i++) {

            if (arr[i] == target) {
                return i;
            }

        }

        return -1;
    }
}
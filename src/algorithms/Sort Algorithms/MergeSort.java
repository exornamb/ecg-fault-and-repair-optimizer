public class MergeSort {

    public static void sort(int[] arr) {
        mergeSort(arr, 0, arr.length - 1);
    }

    private static void mergeSort(int[] arr, int left, int right) {

        if (left < right) {

            int mid = (left + right) / 2;

            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);

            merge(arr, left, mid, right);
        }
    }

    private static void merge(int[] arr, int left, int mid, int right) {

        int[] leftPart = new int[mid - left + 1];
        int[] rightPart = new int[right - mid];

        System.arraycopy(arr, left + 0, leftPart, 0, leftPart.length);

        for (int i = 0; i < rightPart.length; i++)
            rightPart[i] = arr[mid + 1 + i];

        int i = 0, j = 0, k = left;

        while (i < leftPart.length && j < rightPart.length) {

            if (leftPart[i] <= rightPart[j]) {
                arr[k++] = leftPart[i++];
            } else {
                arr[k++] = rightPart[j++];
            }

        }

        while (i < leftPart.length)
            arr[k++] = leftPart[i++];

        while (j < rightPart.length)
            arr[k++] = rightPart[j++];
    }
}
import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;


public class SearchSortTest {

    private static int passed = 0;
    private static int failed = 0;

   
    private static final String[] SORT_NAMES = {"selectionSort", "insertionSort", "mergeSort", "quicksort"};
    @SuppressWarnings("unchecked")
    private static final Function<int[], int[]>[] SORTS = new Function[]{
            (Function<int[], int[]>) Algorithms::selectionSort,
            (Function<int[], int[]>) Algorithms::insertionSort,
            (Function<int[], int[]>) Algorithms::mergeSort,
            (Function<int[], int[]>) Algorithms::quicksort,
    };

    public static void main(String[] args) {
        // ---------- Search tests ----------
        testLinearSearchFindsElement();
        testLinearSearchMissingElement();
        testLinearSearchEmptyArray();
        testLinearSearchFirstOccurrence();
        testBinarySearchFindsElement();
        testBinarySearchMissingElement();
        testBinarySearchEdges();
        testBinarySearchMatchesLinearOnRandomData();

       
        testSortBasic();
        testSortEdgeCases();
        testSortNegativeNumbers();
        testSortDoesNotMutateInput();
        testSortRandomDataMatchesBuiltin();

        System.out.println("\n========================================");
        System.out.printf("Results: %d passed, %d failed%n", passed, failed);
        System.exit(failed == 0 ? 0 : 1);
    }



    static void testLinearSearchFindsElement() {
        check("linearSearch finds element",
                Algorithms.linearSearch(new int[]{4, 2, 7, 1, 9}, 7) == 2);
    }

    static void testLinearSearchMissingElement() {
        check("linearSearch returns -1 when missing",
                Algorithms.linearSearch(new int[]{4, 2, 7, 1, 9}, 5) == -1);
    }

    static void testLinearSearchEmptyArray() {
        check("linearSearch on empty array",
                Algorithms.linearSearch(new int[]{}, 3) == -1);
    }

    static void testLinearSearchFirstOccurrence() {
        check("linearSearch returns first occurrence",
                Algorithms.linearSearch(new int[]{3, 8, 3, 3}, 3) == 0);
    }

    static void testBinarySearchFindsElement() {
        check("binarySearch finds element",
                Algorithms.binarySearch(new int[]{1, 3, 5, 7, 9, 11}, 9) == 4);
    }

    static void testBinarySearchMissingElement() {
        check("binarySearch returns -1 when missing",
                Algorithms.binarySearch(new int[]{1, 3, 5, 7, 9, 11}, 6) == -1);
    }

    static void testBinarySearchEdges() {
        int[] arr = {2, 4, 6, 8, 10};
        check("binarySearch first element", Algorithms.binarySearch(arr, 2) == 0);
        check("binarySearch last element", Algorithms.binarySearch(arr, 10) == 4);
        check("binarySearch empty array", Algorithms.binarySearch(new int[]{}, 1) == -1);
        check("binarySearch single element", Algorithms.binarySearch(new int[]{5}, 5) == 0);
    }

    static void testBinarySearchMatchesLinearOnRandomData() {
        Random rng = new Random(42);
        boolean ok = true;
        for (int t = 0; t < 50; t++) {
            int[] arr = rng.ints(40, 0, 1000).distinct().sorted().toArray();
            int target = rng.nextInt(1000);
            if (Algorithms.binarySearch(arr, target) != Algorithms.linearSearch(arr, target)) {
                ok = false;
                break;
            }
        }
        check("binarySearch matches linearSearch on random sorted data", ok);
    }

   

    static void testSortBasic() {
        forEachSort((name, sort) -> check(name + " basic",
                Arrays.equals(sort.apply(new int[]{5, 2, 9, 1, 5, 6}),
                              new int[]{1, 2, 5, 5, 6, 9})));
    }

    static void testSortEdgeCases() {
        forEachSort((name, sort) -> {
            check(name + " empty", Arrays.equals(sort.apply(new int[]{}), new int[]{}));
            check(name + " single element", Arrays.equals(sort.apply(new int[]{42}), new int[]{42}));
            check(name + " already sorted", Arrays.equals(sort.apply(new int[]{1, 2, 3}), new int[]{1, 2, 3}));
            check(name + " reverse sorted", Arrays.equals(sort.apply(new int[]{3, 2, 1}), new int[]{1, 2, 3}));
            check(name + " all duplicates", Arrays.equals(sort.apply(new int[]{7, 7, 7, 7}), new int[]{7, 7, 7, 7}));
        });
    }

    static void testSortNegativeNumbers() {
        forEachSort((name, sort) -> check(name + " negative numbers",
                Arrays.equals(sort.apply(new int[]{-3, 10, 0, -50, 7}),
                              new int[]{-50, -3, 0, 7, 10})));
    }

    static void testSortDoesNotMutateInput() {
        forEachSort((name, sort) -> {
            int[] original = {3, 1, 2};
            sort.apply(original);
            check(name + " does not mutate input", Arrays.equals(original, new int[]{3, 1, 2}));
        });
    }

    static void testSortRandomDataMatchesBuiltin() {
        Random rng = new Random(7);
        forEachSort((name, sort) -> {
            boolean ok = true;
            for (int t = 0; t < 20; t++) {
                int[] arr = rng.ints(rng.nextInt(61), -100, 101).toArray();
                int[] expected = arr.clone();
                Arrays.sort(expected);
                if (!Arrays.equals(sort.apply(arr), expected)) {
                    ok = false;
                    break;
                }
            }
            check(name + " random data matches Arrays.sort", ok);
        });
    }

   

    interface SortCase {
        void run(String name, Function<int[], int[]> sort);
    }

    static void forEachSort(SortCase testCase) {
        for (int i = 0; i < SORTS.length; i++) {
            testCase.run(SORT_NAMES[i], SORTS[i]);
        }
    }

    static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS  " + name);
        } else {
            failed++;
            System.out.println("FAIL  " + name);
        }
    }
}

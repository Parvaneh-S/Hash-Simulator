/**
 * Comp3760 - Lab3
 * <p>
 * Name: Parvaneh Saberijosheghani
 * Student ID: A01426551
 * Set: 3M
 * <p>
 * This class simulates inserting strings into a hash table using
 * closed hashing with linear probing.
 */

public class HashSimulator {

    /**
     * Runs the hash simulation three times using H1, H2, and H3.
     * <p>
     * results[0] = collisions for H1
     * results[1] = probes for H1
     * results[2] = collisions for H2
     * results[3] = probes for H2
     * results[4] = collisions for H3
     * results[5] = probes for H3
     *
     * @param data the array of strings to hash
     * @param tableSize the size of the hash table
     * @return array of 6 integers containing collisions/probes for H1, H2, H3
     */
    public int[] runHashSimulation(String[] data, int tableSize)
    {
        int[] results = new int[6];

        int[] h1Results = simulateWithHashFunction(data, tableSize, 1);
        results[0] = h1Results[0];
        results[1] = h1Results[1];

        int[] h2Results = simulateWithHashFunction(data, tableSize, 2);
        results[2] = h2Results[0];
        results[3] = h2Results[1];

        int[] h3Results = simulateWithHashFunction(data, tableSize, 3);
        results[4] = h3Results[0];
        results[5] = h3Results[1];

        return results;
    }

    /**
     * Computes the hash value of a name using the H1 hash function.
     * <p>
     * This hash function assigns each uppercase letter a numeric value:
     * A = 1, B = 2, C = 3, ..., Z = 26.
     * It then sums the values of all characters in the string and
     * returns the result modulo the hash table size.
     * <p>
     *
     * @param name the string key to be hashed
     * @param HTsize the size of the hash table
     * @return the hash index in the range 0 to HTsize-1
     */
    public int H1(String name, int HTsize)
    {
        int name_size = name.length();
        int sum = 0;
        for (int i=0 ; i < name_size; i++){
            sum += (name.charAt(i)-'A'+1);
        }
        return (sum%HTsize);
    }


    /**
     * Computes the hash value of a name using the H2 hash function.
     * <p>
     * This hash function treats the string like a base-26 polynomial.
     * Each character is converted to a numeric value (A=1, B=2, ..., Z=26)
     * and multiplied by 26 raised to the power of its position in the string.
     * The position index starts at 0 for the first character.
     * <p>
     * The formula used is:
     * <p>
     * H2(name, HTsize) =
     * ( Σ (letterValue × 26^i) ) mod HTsize
     * <p>
     * The results are summed and then reduced modulo the hash table size.
     * <p>
     * A long type is used for the intermediate sum to reduce the chance
     * of integer overflow when computing large powers of 26.
     *
     * @param name the string key to be hashed
     * @param HTsize the size of the hash table
     * @return the hash index in the range 0 to HTsize - 1
     */
    public int H2(String name, int HTsize){
        int name_size = name.length();
        long sum = 0L;
        for (int i=0 ; i<name_size ; i++){
            sum += (long) ((name.charAt(i)-'A'+1)*(Math.pow(26,i)));
        }
        return (int) (sum%HTsize);
    }

    /**
     * H3(name, HTsize):
     * This hash function is based on the polynomial rolling hash idea.
     * <p>
     * Source/inspiration:
     * A common textbook / widely used idea in hashing is the polynomial rolling hash,
     * where the current hash is multiplied by a constant base and the next character
     * value is added. This implementation uses base 31:
     * <p>
     *     hash = hash * 31 + letterValue
     * <p>
     * and reduces mod HTsize at each step.
     * <p>
     * I chose this because it usually spreads strings better than a simple sum,
     * while still being easy to implement and explain.
     *
     * @param name the string to hash
     * @param HTsize the hash table size
     * @return hash value in range 0..HTsize-1
     */
    public int H3(String name, int HTsize)
    {
        long hash = 0L;

        for (int i = 0; i < name.length(); i++)
        {
            hash = (hash * 31L + (name.charAt(i)-'A'+1)) % HTsize;
        }

        return (int)hash;
    }

    //Helper methods used in runHashSimulation

    /**
     * Simulates inserting all strings using one selected hash function.
     * <p>
     * Returned array:
     * [0] = collisions
     * [1] = probes
     */
    private int[] simulateWithHashFunction(String[] data, int tableSize, int functionNumber)
    {
        String[] table = new String[tableSize];
        int collisions = 0;
        int probes = 0;

        for (int i = 0; i < data.length; i++)
        {
            String key = data[i];
            int index = getHashValue(key, tableSize, functionNumber);

            if (table[index] == null)
            {
                table[index] = key;
            }
            else
            {
                // One collision maximum per item
                collisions++;

                int probeIndex = (index + 1) % tableSize;

                // Count one probe for each bucket examined after the initial collision,
                // including the empty bucket where insertion finally happens.
                while (table[probeIndex] != null)
                {
                    probes++;
                    probeIndex = (probeIndex + 1) % tableSize;
                }

                probes++; // count the final empty bucket examined
                table[probeIndex] = key;
            }
        }

        return new int[] {collisions, probes};
    }

    /**
     * Returns the hash value using H1, H2, or H3.
     */
    private int getHashValue(String key, int tableSize, int functionNumber)
    {
        if (functionNumber == 1)
        {
            return H1(key, tableSize);
        }
        else if (functionNumber == 2)
        {
            return H2(key, tableSize);
        }
        else
        {
            return H3(key, tableSize);
        }
    }

}


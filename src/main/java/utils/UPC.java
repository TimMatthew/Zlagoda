package utils;

import services.StoreProductService;

import java.util.Random;

public class UPC {
    public static String generateRandomUPC(int length) {
        length -= 2;
        Random random = new Random();
        StringBuilder upcBuilder = new StringBuilder("1");
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(length);
            upcBuilder.append(digit);
        }
        int checksum = calculateUPCChecksum(upcBuilder.toString());
        upcBuilder.append(checksum);
        if (!new StoreProductService().isUniqueUPC(upcBuilder.toString()))
            return generateRandomUPC(length+2);
        return upcBuilder.toString();
    }

    private static int calculateUPCChecksum(String upc) {
        int sumOdd = 0;
        int sumEven = 0;
        for (int i = 0; i < upc.length() - 1; i++) {
            int digit = Character.getNumericValue(upc.charAt(i));
            if ((i + 1) % 2 == 0) {
                sumEven += digit;
            } else {
                sumOdd += digit;
            }
        }
        int total = sumOdd * 3 + sumEven;
        int checksum = 10 - (total % 10);
        return (checksum == 10) ? 0 : checksum;
    }
}

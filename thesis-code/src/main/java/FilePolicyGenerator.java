import com.n1analytics.paillier.EncryptedNumber;
import com.n1analytics.paillier.PaillierContext;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FilePolicyGenerator {
    static int alpha = 50;
    static BigInteger M = BigInteger.valueOf(5001);
    List<String> words;
    HashMap<String, Integer> dictionary = new HashMap<>();
    HashMap<String, Integer> cnt = new HashMap<>();


    public FilePolicyGenerator(String dictionaryFile) throws IOException {

        words = Arrays.asList(IOUtils.resourceToString(dictionaryFile, Charset.defaultCharset(), Main.class.getClassLoader()).split("\\s+"));

        for (int i = 0; i < words.size(); i++) {
            System.out.println(words.get(i));
            dictionary.put(words.get(i), i);
        }
    }

    public EncryptedNumber[] filePolicyEncrypt(String fileName, PaillierContext context) throws IOException {

        int[] frequency = new int[dictionary.size()];
        var ref = new Object() {
            int maxFrequency = 1;
        };
        System.out.println("Filename is " + fileName);
        Files.lines(Path.of(fileName), StandardCharsets.ISO_8859_1).forEach(line -> {
//            System.out.println("\t" + line);
            line = line.toLowerCase();
            for (String word : line.split("[\\p{Punct}\\s]+")) {
                cnt.put(word, cnt.getOrDefault(word, 0) + 1);
                if (dictionary.containsKey(word)) {
                    frequency[dictionary.get(word)]++;
                    ref.maxFrequency = Math.max(frequency[dictionary.get(word)], ref.maxFrequency);

                }

            }
        });

        EncryptedNumber[] cipherText = new EncryptedNumber[dictionary.size()];
        //System.out.print(" plaintext");
        for (int i = 0; i < dictionary.size(); i++) {
            long temp = Math.round(((double) alpha * frequency[i]) / ref.maxFrequency);
            BigInteger plainText = M.pow((int) temp);
           // System.out.print(" " + plainText);
            cipherText[i] = context.encrypt(plainText);
        }
        System.out.println();
        return cipherText;
    }
}
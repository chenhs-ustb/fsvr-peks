import com.n1analytics.paillier.EncryptedNumber;
import com.n1analytics.paillier.PaillierContext;
import com.n1analytics.paillier.PaillierPrivateKey;
import com.n1analytics.paillier.PaillierPublicKey;
import org.springframework.util.StopWatch;
import tree.Tree;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    private static String dictionaryFile = "dictionary.txt";

    public static void main(String[] args) throws IOException {
        if(args.length >= 1) dictionaryFile = args[0];
//        System.setErr(new PrintStream(OutputStream.nullOutputStream()));
        /*String publicKeyString = IOUtils.resourceToString("publicKey.pub", Charset.defaultCharset(), Main.class.getClassLoader());
        // System.out.println(publicKeyString);
        String privateKeyString = IOUtils.resourceToString("privateKey.priv", Charset.defaultCharset(), Main.class.getClassLoader());
        // System.out.println(privateKeyString);

        final ObjectMapper mapper = new ObjectMapper();
        final Map publicKey = mapper.readValue(publicKeyString, Map.class);
        final Map privateKey = mapper.readValue(privateKeyString, Map.class);

        PaillierPublicKey pub = SerialisationUtil.unserialise_public(publicKey);
        PaillierPrivateKey priv = SerialisationUtil.unserialise_private(privateKey);*/

        PaillierPrivateKey priv = PaillierPrivateKey.create(704);
        PaillierPublicKey pub = priv.getPublicKey();

        PaillierContext signedContext = pub.createSignedContext();
        FilePolicyGenerator filePolicyGenerator = new FilePolicyGenerator(dictionaryFile);
        StopWatch stopWatch = new StopWatch();
        Tree tree = new Tree();
        Scanner user_input = new Scanner(System.in);
        Path dir;

//        String docPath = "E:\\Thesis Data\\test\\";
//        String docPath = "E:\\Thesis Data\\rfc\\";
        // String docPath = "E:\\Thesis Data\\rfc\\sizeTest\\";
        System.out.println("Enter documents path");
         String docPath = user_input.nextLine();
        dir = Paths.get(docPath);
//        User user = new User(tree, docPath, dictionaryFile);
        /*stopWatch.start();
        EncryptedNumber[] filePolicyEncrypted = filePolicyGenerator.filePolicyEncrypt("E:\\Thesis Data\\testt\\rfc8628.txt", signedContext);
        stopWatch.stop();

        System.out.println(SerialisationUtil.serialise_encrypted(filePolicyEncrypted[0]));
        System.out.println("Time to encrypt a file " + stopWatch.getLastTaskInfo().getTimeMillis());*/


        long averageEncryptTime = 0;
        long averageInsertTime = 0;
        long averagePolicySize = 0;
        double t1000 = 0, t4000 = 0, t7000 = 0;
        int numberFile = 0;
        long size1000=0;
        long size2500=0;
        long size4000=0;
        long size5500=0;
        long size7000=0;
        long size8500=0;
        long startSize= getMemory();
        EncryptedNumber[] data;
        String id, searchQuery;

        System.gc();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.txt")) {
            for (Path entry : stream) {

                stopWatch.start();
                data = filePolicyGenerator.filePolicyEncrypt(docPath + entry.getFileName(), signedContext);
                stopWatch.stop();
                id = entry.getFileName().toString();
                /*int policySize = 0;
                for (EncryptedNumber encryptedNumber : data) {
                    policySize += SerialisationUtil.serialise_encrypted(encryptedNumber).toString().length();
//                    System.out.println("\t" + encryptedNumber + " " + SerialisationUtil.serialise_encrypted(encryptedNumber) + " " + policySize);
                }
                averagePolicySize = averagePolicySize + policySize;*/
                //System.out.println("\nTime to encrypt a file " + stopWatch.getLastTaskInfo().getTimeMillis());
                averageEncryptTime = averageEncryptTime + stopWatch.getLastTaskInfo().getTimeMillis();

                stopWatch.start();
                tree.insert(data, id);
                stopWatch.stop();
                //  System.out.println("\nTime to add a policy to tree " + stopWatch.getLastTaskInfo().getTimeMillis());
                averageInsertTime = averageInsertTime + stopWatch.getLastTaskInfo().getTimeMillis();
                numberFile++;
                if (numberFile == 1000) {
                    t1000 = (double) averageInsertTime / numberFile;
                    size1000 =getMemory();
                }
                if (numberFile == 2500) {
                    t7000 = (double) averageInsertTime / numberFile;
                    size2500 =getMemory();
                }
                if (numberFile == 4000) {
                    t4000 = (double) averageInsertTime / numberFile;
                    size4000 =getMemory();
                }
                if (numberFile == 5500) {
                    t7000 = (double) averageInsertTime / numberFile;
                    size5500 =getMemory();
                }
                if (numberFile == 7000) {
                    t7000 = (double) averageInsertTime / numberFile;
                    size7000 =getMemory();
                }
                if (numberFile == 8450) {
                    t7000 = (double) averageInsertTime / numberFile;
                    size8500 =getMemory();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        filePolicyGenerator.cnt.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).forEach(a -> System.out.println(a.getKey() + " " + a.getValue()));
        /*
        System.out.println("average time for encrypt   " + (double) averageEncryptTime / numberFile);
        System.out.println(" average size for encrypt  " + (double) averagePolicySize / numberFile);
        System.out.println("average time for 1000 file insert    " + t1000);
        System.out.println("average time for 4000 file insert    " + t4000);
        System.out.println("average time for 7000 file insert    " + t7000);
        System.out.println("average time for 10000 insert    " + (double) averageInsertTime / numberFile);
*/
        System.out.println("number of file  " + numberFile);
        System.out.println("number of node is   " + tree.countNode());
        System.out.println("index size after 1000 insert :" + (size1000-startSize));
        System.out.println("index size after 2500 insert :" + (size2500-startSize));
        System.out.println("index size after 4000 insert :" + (size4000-startSize));
        System.out.println("index size after 5500 insert :" + (size5500-startSize));
        System.out.println("index size after 7000 insert :" + (size7000-startSize));
        System.out.println("index size after 8500 insert :" + (size8500-startSize));
        System.out.println("Dictionary size is    " + filePolicyGenerator.dictionary.size());
        System.out.println("M is    " + FilePolicyGenerator.M);
        System.out.println("alpha is    " + FilePolicyGenerator.alpha);
//        user.idf.forEach((key, value) -> System.out.println(key + ": " + value)); //IDF map

       /*
        //single keyword search
        System.out.println("Enter a keyword");
        while (user_input.hasNext()) {
            searchQuery = user_input.nextLine();
            if (searchQuery.equals("quit")) {
                break;
            }
            System.out.println("The file name is = " + user.searchFile(searchQuery));
            System.out.println("Enter a keyword");
        }

        //single keyword search
        System.out.println("Enter a keyword");
        while (user_input.hasNext()) {
            searchQuery = user_input.nextLine();
            if (searchQuery.equals("quit")) {
                break;
            }
            PriorityQueue<Map.Entry<Double, String>> q = user.searchRankedK(searchQuery, 1);
            while (!q.isEmpty()){
                System.out.println(q.peek().getValue() + " : " + q.peek().getKey());
                q.poll();
            }
            System.out.println("Enter a keyword");
        }*/

    }
    public static long getMemory(){
       return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }



}


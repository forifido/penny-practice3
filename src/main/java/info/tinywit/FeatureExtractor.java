package info.tinywit;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class FeatureExtractor {
    public static Map<String, String> initRuleList() {
        LinkedHashMap<String, String> ruleList = new LinkedHashMap<>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(FeatureExtractor.class.getResourceAsStream("/rule.txt"), StandardCharsets.UTF_8));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] rl = StringUtils.split(line, ":");
                if (rl == null) {
                    continue;
                }
                for (int i = 0; i < rl.length; i++) {
                    String[] r = StringUtils.split(rl[i], " ");
                    if (r.length > 1) {
                        ruleList.put(StringUtils.trim(r[0]), StringUtils.trim(r[1]));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                }
            }
        }

        return ruleList;
    }

    public static String extract(String... textList) {
        Map<String, String> ruleList = initRuleList();
        StringBuilder sb = new StringBuilder();
        for (String text : textList) {
            if (sb.length() != 0) {
                sb.append(System.lineSeparator());
            }
            sb.append(extract(text, ruleList));
        }

        return sb.toString();
    }

    public static String extract(String text, Map<String, String> ruleList) {
        String[] sentences = StringUtils.split(text, ",，");
        StringBuilder sb = new StringBuilder();
        if (sentences != null) {
            for (int i = 0; i < sentences.length; i++) {
                String tmp = extractFromSentence(sentences[i], ruleList);
                if (StringUtils.isBlank(tmp)) {
                    continue;
                }
                if (sb.length() != 0) {
                    sb.append(";");
                }
                sb.append(tmp);
            }
        }

        return sb.toString();
    }

    private static String extractFromSentence(String sentence, Map<String, String> ruleList) {
        String ret = "";
        for (Map.Entry<String, String> entry : ruleList.entrySet()) {
            if (Pattern.matches(pattern(entry.getKey()), sentence)) {
                ret = entry.getKey() + " " + entry.getValue();
                break;
            }
        }

        return ret;
    }

    private static String pattern(String rule) {
        return StringUtils.replace("." + rule + ".", ".", ".*");
    }
}

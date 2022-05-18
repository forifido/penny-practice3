package info.tinywit;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class FeatureExtractor {
    private static final List<String> ruleList = new LinkedList<>();

    static {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(FeatureExtractor.class.getResourceAsStream("/rule.txt"), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] rl = StringUtils.split(line, ":");
                if (rl != null) {
                    ruleList.addAll(Arrays.asList(rl));
                    ruleList.add("");
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
    }

    public static String extract(String... textList) {
        StringBuilder sb = new StringBuilder();
        for (String text : textList) {
            append(sb, extract(text, ruleList), System.lineSeparator());
        }
        return sb.toString();
    }

    private static String extract(String text, List<String> ruleList) {
        String[] sentences = StringUtils.split(text, ",，");
        StringBuilder sb = new StringBuilder();
        if (sentences != null) {
            for (int i = 0; i < sentences.length; i++) {
                String tmp = extractFromSentence(sentences[i], ruleList);
                if (!StringUtils.isBlank(tmp)) {
                    append(sb, tmp, ";");
                }
            }
        }
        return sb.toString();
    }

    private static String extractFromSentence(String sentence, List<String> ruleList) {
        boolean found = false;
        StringBuilder sb = new StringBuilder();
        for (String rule : ruleList) {
            if (!found || StringUtils.isBlank(rule)) {
                String[] rp = StringUtils.split(rule, " ", 2);
                if (found = StringUtils.isNotBlank(rule) && Pattern.matches(pattern(rp[0]), sentence)) {
                    append(sb, rp[0] + " " + rp[1], ";");
                }
            }
        }
        return sb.toString();
    }

    private static String pattern(String rule) {
        return StringUtils.replace("." + rule + ".", ".", ".*");
    }

    private static StringBuilder append(StringBuilder sb, String str, String sep) {
        return (sb.length() == 0 ? sb : sb.append(sep)).append(str);
    }
}

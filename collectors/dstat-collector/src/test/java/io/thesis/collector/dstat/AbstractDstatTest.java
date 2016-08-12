package io.thesis.collector.dstat;

import org.junit.Before;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractDstatTest {

    private static final String DSTAT_DATA = "dstat.data";

    private List<String> inputs;

    @Before
    public void setUp() throws IOException {
        inputs = createInputs(DSTAT_DATA);
        assertThat(inputs.size()).isEqualTo(65);
        assertThat(inputs.isEmpty()).isFalse();
    }

    protected List<String> getInputData() {
        return inputs;
    }

    private List<String> createInputs(final String path) throws IOException {
        try (BufferedReader buffer = new BufferedReader(readFromResource(path))) {
            final List<String> allLines = buffer.lines().collect(Collectors.toList());
            final String firstLine = allLines.get(0);
            final String secondLine = allLines.get(1);
            return allLines.stream()
                    .filter(line -> Character.isDigit(line.charAt(0)))
                    .map(dataLine -> firstLine + System.lineSeparator() + secondLine + System.lineSeparator() + dataLine)
                    .collect(Collectors.toList());
        }
    }

    private static InputStreamReader readFromResource(final String resourcePath) throws UnsupportedEncodingException {
        final InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        return new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8.name());
    }
}

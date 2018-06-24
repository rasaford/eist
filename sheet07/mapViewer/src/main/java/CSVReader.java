import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVReader {

  private List<String> lines;
  private List<String> attributes;
  private String valueSeparator;

  public CSVReader(String path, String valueSeparator) {
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(getClass().getResourceAsStream("/" + path)))
    ) {
      this.valueSeparator = valueSeparator;
      String line;
      List<String> lines = new ArrayList<>();
      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
      this.lines = lines;
      String attrs = lines.get(0);
      lines.remove(0);
      this.attributes = Arrays.asList(attrs.split(valueSeparator));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String[][] getAttributes(String... attributes) {
    List<Integer> indices = new ArrayList<>();
    for (int i = 0; i < this.attributes.size(); i++) {
      String attr = this.attributes.get(i);
      for (String arg : attributes) {
        if (attr.equals(arg)) {
          indices.add(i);
        }
      }
    }
    List<String[]> out = new ArrayList<>();
    for (String line : lines) {
      String[] split = line.split(valueSeparator);
      String[] l = new String[indices.size()];
      int j = 0;
      for (int index : indices) {
        if (index < split.length) {
          l[j++] = split[index];
        }
      }
      out.add(l);
    }
    return out.toArray(new String[0][0]);
  }
}

package common;

import collections.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterPair {
    String key;
    String value;

    public Pair<String, String> asPair(){
        return Pair.of(key, value);
    }
}

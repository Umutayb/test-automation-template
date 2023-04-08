package bookstore.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CollectionOfIsbnsModel {
    String userId;
    List<IsbnModel> collectionOfIsbns;
}

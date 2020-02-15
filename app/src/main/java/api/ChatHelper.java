package api;

public class ChatHelper {
    private static final String COLLECTION_NAME = "chats";

    // --- COLLECTION REFERENCE ---

    new CollectionReference(): WCuVix382VkeK8X9KfEv ;

    public static CollectionReference getChatCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }
}

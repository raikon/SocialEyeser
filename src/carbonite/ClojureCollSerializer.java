package carbonite;

import clojure.lang.RT;
import clojure.lang.Var;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Output;

public abstract class ClojureCollSerializer extends Serializer {
    final Var printCollection;
    
    public ClojureCollSerializer() {
        JavaBridge.requireCarbonite();
        printCollection = RT.var("carbonite.serializer", "print-collection");
    }

    public void write(Kryo kryo, Output output, Object o) {
        printCollection.invoke(kryo, output, o);
    }
}

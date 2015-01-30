package carbonite;

import clojure.lang.RT;
import clojure.lang.Var;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

public class ClojureSeqSerializer extends ClojureCollSerializer {
    final Var readSeq;

    public ClojureSeqSerializer() {
        readSeq = RT.var("carbonite.serializer", "read-seq");
    }

    public Object read(Kryo kryo, Input input, Class aClass) {
        return readSeq.invoke(kryo, input);
    }
}

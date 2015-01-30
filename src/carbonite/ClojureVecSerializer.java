package carbonite;

import clojure.lang.RT;
import clojure.lang.Var;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

public class ClojureVecSerializer extends ClojureCollSerializer {
    final Var readVec;

    public ClojureVecSerializer() {
        readVec = RT.var("carbonite.serializer", "read-vector");
    }

    public Object read(Kryo kryo, Input input, Class aClass) {
        return readVec.invoke(kryo, input);
    }
}

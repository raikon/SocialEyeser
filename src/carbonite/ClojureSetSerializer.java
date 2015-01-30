package carbonite;

import clojure.lang.RT;
import clojure.lang.Var;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

public class ClojureSetSerializer extends ClojureCollSerializer {
    final Var readSet;

    public ClojureSetSerializer() {
        readSet = RT.var("carbonite.serializer", "read-set");
    }

    public Object read(Kryo kryo, Input input, Class aClass) {
        return readSet.invoke(kryo, input);
    }
}

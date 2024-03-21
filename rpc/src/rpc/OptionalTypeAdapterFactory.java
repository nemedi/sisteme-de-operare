package rpc;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class OptionalTypeAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<? super T> rawType = typeToken.getRawType();
        if (rawType != Optional.class) {
            return null;
        }

        final Type elementType = ((ParameterizedType) typeToken.getType()).getActualTypeArguments()[0];
        final TypeAdapter<?> elementAdapter = gson.getAdapter(TypeToken.get(elementType));

        return (TypeAdapter<T>) new OptionalTypeAdapter(elementAdapter);
    }

    private static class OptionalTypeAdapter<E> extends TypeAdapter<Optional<E>> {
        private final TypeAdapter<E> elementAdapter;

        OptionalTypeAdapter(TypeAdapter<E> elementAdapter) {
            this.elementAdapter = elementAdapter;
        }

        @Override
        public void write(JsonWriter out, Optional<E> value) throws java.io.IOException {
            if (value.isPresent()) {
                elementAdapter.write(out, value.get());
            } else {
                out.nullValue();
            }
        }

        @Override
        public Optional<E> read(JsonReader in) throws java.io.IOException {
            JsonToken token = in.peek();
            if (token == JsonToken.NULL) {
                in.nextNull();
                return Optional.empty();
            } else {
                E value = elementAdapter.read(in);
                return Optional.ofNullable(value);
            }
        }
    }
}
package NUDT.protocol;

public interface ProtocolHandler<T> {

	byte[] encode(T t) throws Throwable;

	T decode(byte[] bytes) throws Throwable;
}

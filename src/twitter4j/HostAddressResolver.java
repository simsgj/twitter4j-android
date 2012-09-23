package twitter4j;

import java.io.IOException;

public interface HostAddressResolver {

	@Override
	public int hashCode();

	public String resolve(String host) throws IOException;

	@Override
	public String toString();
}

package me.donggyeong.indexer.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.opensearch.client.RestClient;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenSearchConfig {
	@Value("${opensearch.ssl.trust-store}")
	private String trustStore;

	@Value("${opensearch.ssl.trust-store-password}")
	private String trustStorePassword;

	@Value("${opensearch.host}")
	private String host;

	@Value("${opensearch.port}")
	private int port;

	@Value("${opensearch.scheme}")
	private String scheme;

	@Value("${opensearch.username}")
	private String username;

	@Value("${opensearch.password}")
	private String password;

	@Bean
	public OpenSearchClient openSearchClient() {
		System.setProperty("javax.net.ssl.trustStore", trustStore);
		System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);

		final HttpHost httpHost = new HttpHost(host, port, scheme);

		// NOTE: Don't specify credentials in code.
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(new AuthScope(httpHost),
			new UsernamePasswordCredentials(username, password));

		// Initialize the client with SSL and TLS enabled
		final OpenSearchTransport transport = new RestClientTransport(
			RestClient.builder(httpHost)
				.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
					.setDefaultCredentialsProvider(credentialsProvider)
				).build(), new JacksonJsonpMapper());

		return new OpenSearchClient(transport);
	}
}

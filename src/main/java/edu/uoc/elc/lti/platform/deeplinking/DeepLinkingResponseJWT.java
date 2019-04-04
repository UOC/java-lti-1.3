package edu.uoc.elc.lti.platform.deeplinking;

import edu.uoc.elc.lti.platform.AlgorithmFactory;
import edu.uoc.elc.lti.tool.ClaimsEnum;
import edu.uoc.elc.lti.tool.MessageTypesEnum;
import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.elc.lti.platform.deeplinking.content.Item;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
public class DeepLinkingResponseJWT {
	private final static long _5_MINUTES = 5 * 30 * 1000;
	private final static String AUTHORIZED_PART = "azp";

	private final String platformName;
	private final String toolName;
	private final String azp;

	private final String kid;
	private final String publicKey;
	private final String privateKey;
	private final String deploymentId;
	private final String data;
	private final List<Item> itemList;

	@Setter
	private String message;
	@Setter
	private String log;
	@Setter
	private String errorMessage;
	@Setter
	private String errorLog;

	String build() {
		AlgorithmFactory algorithmFactory = new AlgorithmFactory(publicKey, privateKey);

		final JwtBuilder builder = Jwts.builder()
						.setHeaderParam("kid", kid)
						.setIssuer(toolName)
						.setAudience(platformName)
						.setIssuedAt(new Date())
						.setExpiration(new Date(System.currentTimeMillis() + _5_MINUTES))
						.signWith(algorithmFactory.getPrivateKey())
						.claim(ClaimsEnum.MESSAGE_TYPE.getName(), MessageTypesEnum.LtiDeepLinkingRequest.name())
						.claim(ClaimsEnum.VERSION.getName(), "1.3.0")
						.claim(ClaimsEnum.DEPLOYMENT_ID.getName(), deploymentId)
						.claim(ClaimsEnum.DEEP_LINKING_CONTENT_ITEMS.getName(), itemList);


		if (this.azp != null) {
			builder.claim(AUTHORIZED_PART, this.azp);
		}

		if (this.data != null) {
			builder.claim(ClaimsEnum.DEEP_LINKING_DATA.getName(), this.data);
		}

		if (this.message != null) {
			builder.claim(ClaimsEnum.DEEP_LINKING_MESSAGE.getName(), this.message);
		}

		if (this.log != null) {
			builder.claim(ClaimsEnum.DEEP_LINKING_LOG.getName(), this.log);
		}

		if (this.errorMessage != null) {
			builder.claim(ClaimsEnum.DEEP_LINKING_ERROR_MESSAGE.getName(), this.errorMessage);
		}

		if (this.errorLog != null) {
			builder.claim(ClaimsEnum.DEEP_LINKING_ERROR_LOG.getName(), this.errorLog);
		}

		return builder.compact();
	}
}

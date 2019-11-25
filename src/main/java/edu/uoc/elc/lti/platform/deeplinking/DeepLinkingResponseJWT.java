package edu.uoc.elc.lti.platform.deeplinking;

import edu.uoc.elc.lti.platform.AlgorithmFactory;
import edu.uoc.elc.lti.tool.MessageTypesEnum;
import edu.uoc.elc.lti.tool.claims.ClaimsEnum;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import java.util.Date;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
public class DeepLinkingResponseJWT implements DeepLinkingTokenBuilder {
	private final static long _5_MINUTES = 5 * 30 * 1000;
	private final static String AUTHORIZED_PART = "azp";

	private final String publicKey;
	private final String privateKey;

	@Override
	public String build(DeepLinkingResponse deepLinkingResponse) {
		AlgorithmFactory algorithmFactory = new AlgorithmFactory(publicKey, privateKey);

		final JwtBuilder builder = Jwts.builder()
						.setHeaderParam("kid", deepLinkingResponse.getKid())
						.setIssuer(deepLinkingResponse.getToolName())
						.setAudience(deepLinkingResponse.getPlatformName())
						.setIssuedAt(new Date())
						.setExpiration(new Date(System.currentTimeMillis() + _5_MINUTES))
						.signWith(algorithmFactory.getPrivateKey())
						.claim(ClaimsEnum.MESSAGE_TYPE.getName(), MessageTypesEnum.LtiDeepLinkingRequest.name())
						.claim(ClaimsEnum.VERSION.getName(), "1.3.0")
						.claim(ClaimsEnum.DEPLOYMENT_ID.getName(), deepLinkingResponse.getDeploymentId())
						.claim(ClaimsEnum.DEEP_LINKING_CONTENT_ITEMS.getName(), deepLinkingResponse.getItemList());


		if (deepLinkingResponse.getAzp() != null) {
			builder.claim(AUTHORIZED_PART, deepLinkingResponse.getAzp());
		}

		if (deepLinkingResponse.getData() != null) {
			builder.claim(ClaimsEnum.DEEP_LINKING_DATA.getName(), deepLinkingResponse.getData());
		}

		if (deepLinkingResponse.getMessage() != null) {
			builder.claim(ClaimsEnum.DEEP_LINKING_MESSAGE.getName(), deepLinkingResponse.getMessage());
		}

		if (deepLinkingResponse.getLog() != null) {
			builder.claim(ClaimsEnum.DEEP_LINKING_LOG.getName(), deepLinkingResponse.getLog());
		}

		if (deepLinkingResponse.getErrorMessage() != null) {
			builder.claim(ClaimsEnum.DEEP_LINKING_ERROR_MESSAGE.getName(), deepLinkingResponse.getErrorMessage());
		}

		if (deepLinkingResponse.getErrorLog() != null) {
			builder.claim(ClaimsEnum.DEEP_LINKING_ERROR_LOG.getName(), deepLinkingResponse.getErrorLog());
		}

		return builder.compact();
	}
}

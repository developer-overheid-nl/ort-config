FROM busybox:1.37.0@sha256:9db7b59979c38555a39def84a31fb98b5296952f9e3afd4f6f11f05b07adfab0

ARG SOURCE_REVISION=dev

LABEL org.opencontainers.image.title="DON ORT configuration" \
      org.opencontainers.image.description="Versioned evaluator rules for the DON ORT runner" \
      org.opencontainers.image.source="https://github.com/developer-overheid-nl/ort-config" \
      org.opencontainers.image.revision="${SOURCE_REVISION}"

COPY evaluator.rules.kts /config/evaluator.rules.kts

CMD ["sh", "-c", "cp /config/evaluator.rules.kts /target/evaluator.rules.kts"]

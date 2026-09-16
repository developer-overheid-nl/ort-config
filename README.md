# DON ORT-configuratie

Deze repository bevat de basisregels waarmee de ORT-runner open-sourceprojecten
uit het OSS-register controleert. De configuratie is bewust klein: de runner heeft
alleen [`evaluator.rules.kts`](evaluator.rules.kts) nodig.

## Controles

| Controle | Ernst |
| --- | --- |
| Bekende kwetsbaarheid in een dependency | WARNING |
| `README.md` aanwezig | ERROR |
| `LICENSE` aanwezig | ERROR |
| `publiccode.yml` of `publiccode.yaml` aanwezig | ERROR |
| `CONTRIBUTING.md` aanwezig | ERROR |
| `SECURITY.md` aanwezig | ERROR |
| `CODE_OF_CONDUCT.md` aanwezig | WARNING |
| `CHANGELOG` of `CHANGELOG.md` aanwezig | WARNING |
| Bekende CI-configuratie aanwezig | WARNING |

`README.md`, `LICENSE`, `publiccode.yml` en `CONTRIBUTING.md` worden in de root
verwacht. `SECURITY.md`, `CODE_OF_CONDUCT.md` en `CHANGELOG` mogen ook onder
`.github/` of `docs/` staan. De changelognaam is hoofdletterongevoelig en mag de
extensie `.md` hebben.

De kwetsbaarheidsregel gebruikt Advisor-resultaten van OSV. Details zoals advisory-ID,
score en opgeloste versies staan in `advisor-result.yml` en worden door de runner ook
in `run.json` opgenomen. De bestandsregels controleren aanwezigheid, niet de inhoud.

## Configuratie-image

Een semver-tag met de vorm `v*.*.*` start de releaseworkflow. Deze workflow test de
ruleset, publiceert het image en maakt een GitHub Release. Het image verschijnt als:

```text
ghcr.io/developer-overheid-nl/ort-config:v0.1.0
ghcr.io/developer-overheid-nl/ort-config:<commit-sha>
```

Gebruik voor productie een release-tag of image-digest. Het image kopieert standaard
`evaluator.rules.kts` naar een volume dat op `/target` is gemount.

### Lokaal zonder checkout

```sh
docker volume create ort-config
docker run --rm \
  -v ort-config:/target \
  ghcr.io/developer-overheid-nl/ort-config:v0.1.0

docker run --rm --init \
  --user "$(id -u):$(id -g)" \
  --env-file .env.local \
  -e HOME=/tmp \
  -v ort-config:/config:ro \
  -v "$PWD/output:/output" \
  ort-runner:dev
```

Vervang `v0.1.0` door de gewenste release. Opnieuw uitvoeren overschrijft de ruleset
in het volume met de gekozen versie.

### Kubernetes

Gebruik het config-image als init container en deel een `emptyDir` met de runner:

```yaml
volumes:
  - name: ort-config
    emptyDir: {}

initContainers:
  - name: ort-config
    image: ghcr.io/developer-overheid-nl/ort-config:v0.1.0
    volumeMounts:
      - name: ort-config
        mountPath: /target

containers:
  - name: ort-runner
    image: ghcr.io/developer-overheid-nl/ort-runner:VERSION
    volumeMounts:
      - name: ort-config
        mountPath: /config
        readOnly: true
```

## Ontwikkelen en testen

Pull requests bouwen het image en voeren Analyzer, Advisor en Evaluator uit op een
kleine Git-fixture. Handmatig kan dezelfde imagebouw worden gecontroleerd met:

```sh
docker build -t ort-config:test .
mkdir -p /tmp/ort-config-test
docker run --rm -v /tmp/ort-config-test:/target ort-config:test
test -s /tmp/ort-config-test/evaluator.rules.kts
```

De ruleset is gemaakt voor ORT `92.4.0`. Test een ORT-upgrade eerst in de runner
voordat de vastgezette ORT-image daar wordt gewijzigd.

## Releasen

Net als `don-register-common` gebruikt deze repository een semver Git-tag als trigger:

```sh
git checkout main
git pull
git tag v0.1.0
git push origin v0.1.0
```

De workflow valideert de ruleset voordat het image en de GitHub Release worden
gepubliceerd.

## Licentie

Deze repository is beschikbaar onder de Apache License 2.0. Zie [LICENSE](LICENSE)
en [NOTICE](NOTICE).

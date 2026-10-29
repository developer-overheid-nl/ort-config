# DON ORT-configuratie

Deze repository bevat de basisregels waarmee de ORT-runner open-sourceprojecten
uit het OSS-register controleert. De configuratie is bewust klein: de runner gebruikt
[`evaluator.rules.kts`](evaluator.rules.kts) voor de controles en
[`rules.yml`](rules.yml) om naast de overtredingen ook de geslaagde regels te
rapporteren. Een nieuwe regel hoort in beide bestanden; de PR-check vergelijkt ze.
[`config.yml`](config.yml) is de ORT-configuratie die elke ORT-stap leest.

## ORT-configuratie

De ORT-image draait op JDK 25. Gradle-versies onder 9.1 kunnen daar niet op draaien,
waardoor de analyse van zulke projecten mislukte met `Unsupported class file major
version 69`. `config.yml` laat de Gradle-analyse daarom Java 17 gebruiken; ORT
downloadt die JDK zelf. Java 17 draait Gradle 7.3 tot en met 9.x en is het minimum
voor de Android Gradle Plugin 8.

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

## Ontwikkelen en testen

Pull requests bouwen het image en controleren dat dit exact `evaluator.rules.kts`,
`rules.yml` en `config.yml` naar het configuratievolume kopieert, en dat de eerste
twee bestanden dezelfde regels noemen. Handmatig kan dezelfde imagebouw worden
gecontroleerd met:

```sh
docker build -t ort-config:test .
mkdir -p /tmp/ort-config-test
docker run --rm -v /tmp/ort-config-test:/target ort-config:test
test -s /tmp/ort-config-test/evaluator.rules.kts
test -s /tmp/ort-config-test/rules.yml
test -s /tmp/ort-config-test/config.yml
```

De runner is verantwoordelijk voor de compatibiliteitstest van de ruleset met zijn
vastgezette ORT-versie.

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

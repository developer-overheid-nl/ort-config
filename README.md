# ORT Config

This repository contains [configuration files](https://github.com/oss-review-toolkit/ort#configuration-files) for the
[OSS Review Toolkit](https://github.com/oss-review-toolkit/ort).

## OSS-register: basisregels

In [evaluator.rules.kts](evaluator.rules.kts) is alleen `ossRegisterBaselineRules()` actief.
Dit profiel roept de bestaande README-, LICENSE-, CONTRIBUTING- en CI-regels aan, aangevuld met vier nieuwe regels.
De aanroepen van de overige policygroepen zijn uitgecomment; hun definities en de overige configuratie blijven behouden.

| Controle | Melding bij ontbreken |
| --- | --- |
| README.md aanwezig | ERROR |
| LICENSE aanwezig | ERROR |
| publiccode.yml of publiccode.yaml aanwezig | ERROR |
| CONTRIBUTING.md aanwezig | ERROR |
| SECURITY.md aanwezig | ERROR |
| CODE_OF_CONDUCT.md aanwezig | WARNING |
| CHANGELOG / CHANGELOG.md aanwezig | WARNING |
| CI-configuratie aanwezig | WARNING |

De bestaande bestandsregels zoeken `README.md`, `LICENSE` en `CONTRIBUTING.md` in de root.
Ook publiccode staat in de root. SECURITY, CODE_OF_CONDUCT en CHANGELOG mogen daarnaast in `.github/` of `docs/` staan.
CHANGELOG wordt met of zonder `.md` en ongeacht hoofdletters herkend. De overige bestandsnamen zijn hoofdlettergevoelig.
De CI-regel herkent bekende configuratiebestanden of -mappen; een ontbrekende CI geeft in dit profiel een waarschuwing.
Deze regels controleren uitsluitend aanwezigheid, niet de inhoud of het slagen van CI-runs.

Gebruik de bestaande Docker-commando's voor Analyzer, Advisor en Evaluator met
`--rules-file /home/ort/.ort/config/evaluator.rules.kts`.
Er zijn geen extra installatiecommando's of dependencies nodig voor deze basisregels.
ORT haalt de repositoryrevisie uit het analyseresultaat op voor de bestandscontroles.
Publiccode wordt in deze begintset alleen op aanwezigheid gecontroleerd.

## Content

### Curations

The [curations](./curations/) directory contains
[package curations](https://github.com/oss-review-toolkit/ort/blob/main/docs/config-file-curations-yml.md) for
open source packages.

Package curations submitted to this repository must adhere to the following rules:

* Declaring authors and concluded licenses is currently not allowed.
* Declared license mappings must map licenses to valid SPDX expressions. The curation comment must provide proof that
  the mapping is correct.
* Curations that apply to whole namespaces by only setting the type and namespace of the identifier are not allowed.
* The curation file path must be `curations/[type]/[namespace]/[name].yml`. If the namespace is empty, use "_". For
  example a curation for the package `NuGet::Azure.Core:1.2.0` must be in the file `curation/NuGet/_/Azure.Core.yml`.

Package configurations containing license finding curations or path excludes are not yet supported.

### Tools

The [tools](./tools/) directory contains tools that help generating curations.

## Usage

To use the configuration provided by this repository, it needs to be cloned, and the files need to be passed to the
respective options of the ORT CLI commands. For example, to use the curations with the ORT analyzer:

```
ort analyze --package-curations-dir [path-to-curations-dir]
```

Using this repository together with ORT will be simplified in future.

## Contribute

This repository is currently in incubation and not yet ready for contributions.

# License

Copyright (C) 2019-2024 [The ORT Project Authors](./NOTICE).

See the [LICENSE](./LICENSE) file in the root of this project for license details.

OSS Review Toolkit (ORT) is a [Linux Foundation project](https://www.linuxfoundation.org) and part of
[ACT](https://automatecompliance.org/).

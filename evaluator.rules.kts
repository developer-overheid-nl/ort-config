/*
 * Copyright (C) 2019 The ORT Project Authors (see <https://github.com/oss-review-toolkit/ort-config/blob/main/NOTICE>)
 * Copyright (C) 2026 Developer Overheid NL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * License-Filename: LICENSE
 */

fun RuleSet.vulnerabilityInDependencyRule() = packageRule("VULNERABILITY_IN_DEPENDENCY") {
    require {
        -isProject()
        -isExcluded()
        +hasVulnerability()
    }

    warning(
        message = "The package '${pkg.metadata.id.toCoordinates()}' has a vulnerability.",
        howToFix = "Review the OSV advisory in advisor-result.yml and update or replace the dependency."
    )
}

fun RuleSet.missingReadmeFileRule() = projectSourceRule("MISSING_README_FILE") {
    require {
        -projectSourceHasFile("README.md")
    }

    error("The project's code repository does not contain the file 'README.md'.")
}

fun RuleSet.missingLicenseFileRule() = projectSourceRule("MISSING_LICENSE_FILE") {
    require {
        -projectSourceHasFile("LICENSE")
    }

    error("The project's code repository does not contain the file 'LICENSE'.")
}

fun RuleSet.missingPubliccodeFileRule() = projectSourceRule("MISSING_PUBLICCODE_FILE") {
    require {
        -projectSourceHasFile("publiccode.yml", "publiccode.yaml")
    }

    error("The project's code repository does not contain a 'publiccode.yml' or 'publiccode.yaml' file.")
}

fun RuleSet.missingContributingFileRule() = projectSourceRule("MISSING_CONTRIBUTING_FILE") {
    require {
        -projectSourceHasFile("CONTRIBUTING.md")
    }

    error("The project's code repository does not contain the file 'CONTRIBUTING.md'.")
}

fun RuleSet.missingSecurityFileRule() = projectSourceRule("MISSING_SECURITY_FILE") {
    require {
        -projectSourceHasFile("SECURITY.md", ".github/SECURITY.md", "docs/SECURITY.md")
    }

    error("The project's code repository does not contain the file 'SECURITY.md'.")
}

fun RuleSet.missingCodeOfConductFileRule() = projectSourceRule("MISSING_CODE_OF_CONDUCT_FILE") {
    require {
        -projectSourceHasFile("CODE_OF_CONDUCT.md", ".github/CODE_OF_CONDUCT.md", "docs/CODE_OF_CONDUCT.md")
    }

    warning("The project's code repository does not contain the file 'CODE_OF_CONDUCT.md'.")
}

fun RuleSet.missingChangelogFileRule() = projectSourceRule("MISSING_CHANGELOG_FILE") {
    val filePattern = "{file:(?i:changelog(?:\\.md)?)}"

    require {
        -projectSourceHasFile(filePattern, ".github/$filePattern", "docs/$filePattern")
    }

    warning("The project's code repository does not contain a 'CHANGELOG' or 'CHANGELOG.md' file.")
}

fun RuleSet.missingCiConfigurationRule() = projectSourceRule("MISSING_CI_CONFIGURATION") {
    require {
        -AnyOf(
            projectSourceHasFile(
                ".appveyor.yml",
                ".bitbucket-pipelines.yml",
                ".gitlab-ci.yml",
                ".travis.yml"
            ),
            projectSourceHasDirectory(
                ".circleci",
                ".github/workflows"
            )
        )
    }

    warning(
        message = "This project does not have any known CI configuration files.",
        howToFix = "Please set up CI. If CI is already configured, add its path to this rule."
    )
}

val ruleSet = ruleSet(ortResult, licenseInfoResolver, resolutionProvider) {
    vulnerabilityInDependencyRule()
    missingReadmeFileRule()
    missingLicenseFileRule()
    missingPubliccodeFileRule()
    missingContributingFileRule()
    missingSecurityFileRule()
    missingCodeOfConductFileRule()
    missingChangelogFileRule()
    missingCiConfigurationRule()
}

ruleViolations += ruleSet.violations

/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.sample

import org.gradle.testkit.runner.GradleRunner
import static org.gradle.testkit.runner.TaskResult.*
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class BuildLogicFunctionalTest extends Specification {
    @Rule final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile

    // START SNIPPET functional-test-classpath-setup
    def setup() {
        buildFile = testProjectDir.newFile('build.gradle')
        buildFile << """
            buildscript {
                dependencies {
                    classpath files('${System.properties['classesDir']}')
                }
            }
        """
    }
    // END SNIPPET functional-test-classpath-setup

    def "hello world task prints hello world"() {
        given:
        buildFile << 'apply plugin: org.gradle.sample.HelloWorldPlugin'

        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments('helloWorld')
            .build()

        then:
        result.standardOutput.contains('Hello world!')
        result.standardError == ''
        result.taskPaths(SUCCESS) == [':helloWorld']
        result.tasks(SKIPPED).empty
        result.tasks(UP_TO_DATE).empty
        result.tasks(FAILED).empty
    }
}

/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.test.nextDeparture;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import com.test.nextDeparture.handlers.CancelandStopIntentHandler;
import com.test.nextDeparture.handlers.FallbackIntentHandler;
import com.test.nextDeparture.handlers.HelpIntentHandler;
import com.test.nextDeparture.handlers.LaunchRequestHandler;
import com.test.nextDeparture.handlers.SessionEndedRequestHandler;
import com.amazon.ask.SkillStreamHandler;

public class NextDepartureStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new CancelandStopIntentHandler(),
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler(),
                        new FallbackIntentHandler())
                .withSkillId("amzn1.ask.skill.233bb8a8-55fb-4da2-a399-65aa3384090e")
                .build();
    }

    public NextDepartureStreamHandler() {
        super(getSkill());
        
    }

}

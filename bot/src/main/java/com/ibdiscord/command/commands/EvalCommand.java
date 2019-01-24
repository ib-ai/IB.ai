package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.utils.UString;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashSet;
import java.util.Set;

/**
 * Copyright 2018 Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class EvalCommand extends Command {

    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    /**
     * Creates the command.
     */
    public EvalCommand() {
        super("eval", Set.of("evaluate", "js", "justgeekythings"), CommandPermission.developer(CommandPermission.discord()), new HashSet<>());
    }

    /**
     * Evaluates the given code.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 1) {
            context.reply("Please give some code to evaluate");
        }
        var code = UString.concat(context.getArguments(), " ", 0);
        engine.put("c", context);
        engine.put("ctx", context);
        engine.put("context", context);
        Object out;
        try {
            out = engine.eval("(function(){with(imports){" + code + "}})();");
        } catch(ScriptException exception) {
            context.reply("Error, stacktrace printed: " + exception.getMessage());
            exception.printStackTrace();
            return;
        }
        var response = UString.stripMassMentions(out.toString());
        if(response.length() > 2000) {
            context.getChannel().sendFile(response.getBytes(), "output_past_threshold.txt")
                    .queue(null, error -> context.reply("Well, it seems as if the output can't be sent as a file."));
            return;
        }
        context.reply("__**Output**__\n\n" + response);
    }

}

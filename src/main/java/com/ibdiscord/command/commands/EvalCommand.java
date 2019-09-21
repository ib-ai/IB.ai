/* Copyright 2017-2019 Arraying
 *
 * This file is part of IB.ai.
 *
 * IB.ai is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * IB.ai is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with IB.ai. If not, see http://www.gnu.org/licenses/.
 */

package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.utils.UString;

import java.util.Set;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public final class EvalCommand extends Command {

    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    /**
     * Creates the command.
     */
    public EvalCommand() {
        super("eval",
                CommandPermission.developer(CommandPermission.discord()),
                Set.of()
        );
        this.correctUsage = "eval <JS code>";
        try {
            engine.eval("var imports = new JavaImporter(java.io, java.lang, java.util, java.net);");
        } catch(ScriptException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Evaluates the given code.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 1) {
            sendUsage(context);
            return;
        }
        String code = UString.concat(context.getArguments(), " ", 0);
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
        String response = UString.stripMassMentions(out.toString());
        if(response.length() > 2000) {
            context.getChannel().sendFile(response.getBytes(), "output_past_threshold.txt")
                    .queue(null, error -> context.reply("Well, it seems as if the output can't "
                            + "be sent as a file.")
            );
            return;
        }
        context.reply("**Output:**```" + response + "```");
    }

}

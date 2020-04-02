/* Copyright 2017-2020 Arraying
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

package com.ibdiscord.command.actions;

import com.ibdiscord.command.CommandAction;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.utils.UString;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public final class Eval implements CommandAction {

    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    /**
     * Does some pre-processing.
     */
    public Eval() {
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
    public void accept(CommandContext context) {
        context.assertArguments(1, "error.generic_arg_length");
        String code = UString.concat(context.getArguments(), " ", 0);
        engine.put("c", context);
        engine.put("ctx", context);
        engine.put("context", context);
        Object out;
        try {
            out = engine.eval("(function(){with(imports){" + code + "}})();");
        } catch(ScriptException exception) {
            context.replyI18n("error.eval_exception", exception.getMessage());
            exception.printStackTrace();
            return;
        }
        String response = UString.stripMassMentions(out.toString());
        if(response.length() > 2000) {
            context.getChannel().sendFile(response.getBytes(), "output_past_threshold.txt")
                    .queue(null, error -> context.replyI18n("error.eval_file"));
            return;
        }
        context.replyI18n("success.eval", response);
    }

}

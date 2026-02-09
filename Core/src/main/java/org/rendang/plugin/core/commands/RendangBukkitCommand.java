package org.rendang.plugin.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.rendang.plugin.core.commands.annotation.AutoComplete;
import org.rendang.plugin.core.commands.annotation.CommandExecute;
import org.rendang.plugin.core.commands.event.CommandEvent;
import java.lang.reflect.Method;
import java.util.*;

public class RendangBukkitCommand extends Command implements TabCompleter, CommandExecutor {
    final HashMap<String, Execution> executor = new HashMap<>();
    final HashMap<String, Execution> completor = new HashMap<>();
    final RendangCommand rendangCommand;

    public RendangBukkitCommand(RendangCommand RendangCommand) {
        super(RendangCommand.getCommandName());
        this.rendangCommand = RendangCommand;
        Class<RendangBukkitCommand> commandsInstanceClass = RendangBukkitCommand.class;
        for (Method method : commandsInstanceClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(CommandExecute.class)) {
                CommandExecute commandExecute = method.getAnnotation(CommandExecute.class);
                executor.put(commandExecute.command(), new Execution(this, method));
            }
            if (method.isAnnotationPresent(AutoComplete.class)) {
                AutoComplete autoComplete = method.getAnnotation(AutoComplete.class);
                completor.put(autoComplete.command(), new Execution(this, method));
            }
        }
        Class<?> clazz = RendangCommand.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(CommandExecute.class)) {
                CommandExecute commandExecute = method.getAnnotation(CommandExecute.class);
                executor.put(commandExecute.command(), new Execution(RendangCommand, method));
            }
            if (method.isAnnotationPresent(AutoComplete.class)) {
                AutoComplete autoComplete = method.getAnnotation(AutoComplete.class);
                completor.put(autoComplete.command(), new Execution(RendangCommand, method));
            }
        }
        for(Execution execution : executor.values()){
            if(execution.method.getReturnType() != boolean.class){
                throw new IllegalArgumentException("CommandExecute method must return boolean");
            }
            execution.method.setAccessible(true);
        }
        for(Execution execution : completor.values()){
            if(execution.method.getReturnType() != List.class){
                throw new IllegalArgumentException("AutoComplete method must return List<String>");
            }
            execution.method.setAccessible(true);
        }
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        return onCommand(sender, this, commandLabel, args);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        List<String> result = onTabComplete(sender, this, alias, args);
        return result != null ? result : Collections.emptyList();
    }

    @CommandExecute(command = "help", usages = "<page>", description = "Shows all commands")
    public boolean helpCommand(CommandEvent event) {
        List<String> commandsString = new ArrayList<>();
        final int max = 5;
        for(String field : executor.keySet()) {
            CommandExecute commandExecute = executor.get(field).method.getAnnotation(CommandExecute.class);
            commandsString.add(commandExecute.command());
        }
        Collections.sort(commandsString);
        int maxPage = (int) Math.ceil(commandsString.size() / (double) max);
        int page;
        if(event.getArgs().length <= 1){
            page = 0;
        } else {
            page = Integer.parseInt(event.getArgs()[1]) - 1;
        }
        if(page < 0 || page >= maxPage){
            event.getSender().sendMessage(ChatColor.DARK_RED + "Unknown chapter");
            return true;
        }

        StringBuilder message = new StringBuilder(ChatColor.GREEN + "---- Help -- Page " + (page + 1) + "/"+ maxPage + "----" + ChatColor.RESET);
        for (int i = page*max; i < max+(page*max); i++) {
            if (i >= commandsString.size()) break;
            CommandExecute commandExecute = executor.get(commandsString.get(i)).method.getAnnotation(CommandExecute.class);
            message.append("\n" + ChatColor.GOLD).append(commandExecute.command())
                    .append(" : /" + rendangCommand.getCommandName() + " ").append(commandExecute.command().toLowerCase(Locale.ROOT)).append(" ")
                    .append(commandExecute.usages()).append(ChatColor.RESET + "\n");
            message.append("  ").append(commandExecute.description());
        }
        if(page + 1 < maxPage){
            message.append("\n" + ChatColor.GREEN + "Type /" + rendangCommand.getCommandName() + " help " + ChatColor.GOLD.toString() + (page+2) + ChatColor.GREEN.toString() + " to see more commands");
        }
        event.getSender().sendMessage(message.toString());
        return true;
    }

    @AutoComplete(command = "help")
    public List<String> helpCompleter(CommandEvent event) {
        return List.of(new String[]{"[number]"});
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {

        } else {
            String rootCommand = args[0];
            if(!executor.containsKey(rootCommand)){
                sender.sendMessage("This command is not found. Please use /" + rendangCommand.getCommandName() + " help to see all commands.");
                return true;
            }
            CommandExecute commandExecute = executor.get(rootCommand).method.getAnnotation(CommandExecute.class);
            if(!commandExecute.permission().isEmpty() && !sender.hasPermission(commandExecute.permission())){
                sender.sendMessage("You don't have permission to use this command");
                return true;
            }
            try {
                CommandEvent event = new CommandEvent(sender, command, label, args);
                Execution execution = executor.get(rootCommand);
                return (boolean) executor.get(rootCommand).method.invoke(execution.context, event);
            } catch (IndexOutOfBoundsException exception) {
                sender.sendMessage("This command format is not valid. Please use /" + rendangCommand.getCommandName() + " help for more info.");
                //exception.printStackTrace();
            } catch (Exception e) {
                sender.sendMessage("This command is invalid. Please use /" + rendangCommand.getCommandName() + " help to see all commands.");
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completion = new ArrayList<>();
        if(!executor.containsKey(args[0])){
            if (args.length > 1) return completion;
            List<String> commands = new ArrayList<>(executor.keySet());
            completion.addAll(stringFilter(commands, args[0]));
        } else {
            if (args.length < 2) return completion;
            String commandName = args[0];
            Execution execution = completor.get(commandName);
            if(execution == null) return completion;
            AutoComplete autoComplete = execution.method.getAnnotation(AutoComplete.class);
            if(autoComplete.permission().isEmpty() || sender.hasPermission(autoComplete.permission())){
                try {
                    List<String> result = (List<String>) execution.method.invoke(execution.context, new CommandEvent(sender, command, alias, args));
                    if(result == null) return null;
                    completion.addAll(stringFilter(result, args[args.length - 1]));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
        return completion;
    }

    public boolean containsIgnoreCase(String string, String contains){
        return string.toLowerCase().contains(contains.toLowerCase());
    }

    List<String> stringFilter(List<String> strings, String filter){
        if(filter.isEmpty()) return strings;
        List<String> filteredStrings = new ArrayList<>();
        for(String string : strings){
            if(containsIgnoreCase(string, filter)){
                filteredStrings.add(string);
            }
        }
        return filteredStrings;
    }

    public class Execution {
        Object context;
        Method method;

        public Execution(Object context, Method method) {
            this.context = context;
            this.method = method;
        }
    }
}

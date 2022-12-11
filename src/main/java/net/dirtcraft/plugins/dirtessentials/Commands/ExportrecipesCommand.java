package net.dirtcraft.plugins.dirtessentials.Commands;

import com.google.gson.Gson;
import net.dirtcraft.plugins.dirtessentials.Data.Ingredient;
import net.dirtcraft.plugins.dirtessentials.Data.Mods;
import net.dirtcraft.plugins.dirtessentials.DirtEssentials;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportrecipesCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.EXPORTRECIPES)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		List<Mods> modRecipes = new ArrayList<>();
		sender.sendMessage(Strings.PREFIX + ChatColor.BLUE + "Exporting recipes ...");

		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			for (Material material : Material.values()) {
				List<Recipe> recipes = Bukkit.getRecipesFor(new ItemStack(material));
				if (recipes.isEmpty()) continue;

				String modNamespace = material.getKey().getNamespace();
				if (modRecipes.stream().noneMatch(mod -> mod.getMod().equals(modNamespace))) {
					modRecipes.add(new Mods(modNamespace));
				}

				Mods mod = modRecipes.stream().filter(mods -> mods.getMod().equals(modNamespace)).findFirst().orElse(null);
				if (mod == null) continue;

				for (Recipe recipe : recipes) {
					if (!(recipe instanceof ShapedRecipe) && !(recipe instanceof ShapelessRecipe)) continue;

					if (recipe instanceof ShapelessRecipe) {
						ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
						mod.getRecipes().add(new net.dirtcraft.plugins.dirtessentials.Data.Recipe(material.getKey().toString(), shapelessRecipe.getResult().getAmount()));

						Map<NamespacedKey, Integer> ingredientMap = new HashMap<>();
						for (ItemStack ingredient : shapelessRecipe.getIngredientList()) {
							if (ingredient == null) continue;
							NamespacedKey key = ingredient.getType().getKey();
							if (ingredientMap.containsKey(key)) {
								ingredientMap.put(key, ingredientMap.get(key) + 1);
							} else {
								ingredientMap.put(key, 1);
							}
						}

						for (Map.Entry<NamespacedKey, Integer> entry : ingredientMap.entrySet()) {
							mod.getRecipes().get(mod.getRecipes().size() - 1).getIngredients().add(new Ingredient(entry.getKey().toString(), entry.getValue()));
						}

						continue;
					}

					ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
					mod.getRecipes().add(new net.dirtcraft.plugins.dirtessentials.Data.Recipe(material.getKey().toString(), shapedRecipe.getResult().getAmount()));

					Map<NamespacedKey, Integer> ingredientMap = new HashMap<>();
					for (String row : shapedRecipe.getShape()) {
						for (char c : row.toCharArray()) {
							ItemStack ingredient = shapedRecipe.getIngredientMap().get(c);
							if (ingredient == null) continue;
							NamespacedKey key = ingredient.getType().getKey();
							if (ingredientMap.containsKey(key)) {
								ingredientMap.put(key, ingredientMap.get(key) + 1);
							} else {
								ingredientMap.put(key, 1);
							}
						}
					}

					for (Map.Entry<NamespacedKey, Integer> entry : ingredientMap.entrySet()) {
						mod.getRecipes().get(mod.getRecipes().size() - 1).getIngredients().add(new Ingredient(entry.getKey().toString(), entry.getValue()));
					}
				}
			}

			Bukkit.getScheduler().runTask(DirtEssentials.getPlugin(), () -> {
				sender.sendMessage(Strings.PREFIX + ChatColor.GREEN + "Exporting recipes finished!");
				try {
					FileWriter writer = new FileWriter(DirtEssentials.getPlugin().getDataFolder() + "/recipes.json");
					Gson gson = new Gson();
					gson.toJson(modRecipes, writer);
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		});


		return true;
	}
}

# Stone Colossus Boss

Adds a large tanky **Stone Colossus** boss to Minecraft 1.20.1 (Fabric). The Colossus:

- Has huge health (250 HP) and heavy armor — a proper tank.
- Chases and attacks any player it sees.
- Uses a **Slam Attack** that deals area damage and launches nearby entities.
- Drops **Colossus Cores** on death.

Craft **Colossus Cores** into a powerful **Colossus Hammer** that hits hard and deals splash damage to enemies around your target.

## Crafting the Colossus Hammer

```
C C C
C S C
  S
```
- **C** = Colossus Core
- **S** = Stick

## How to build the mod (no Java install needed!)

GitHub can build the `.jar` for you for **free** — you don't need to install Java or Gradle on your computer.

### Steps

1. **Create a GitHub account** at https://github.com (if you don't have one).
2. Click the **+** in the top right → **New repository**. Give it any name and click **Create repository**.
3. On the new empty repo page, click **"uploading an existing file"**.
4. **Extract the downloaded zip** of this mod on your computer.
5. **macOS users — IMPORTANT:** the `.github` folder is **hidden** by default in Finder. Press **Cmd + Shift + .** (period) in Finder to show hidden files. If you skip this step, the `.github` folder will NOT be uploaded, the build workflow will never run, and no `.jar` will ever be produced.
6. Open the extracted folder. **Select ALL files and folders from INSIDE the folder** — including the hidden `.github` folder — and **drag them into the GitHub upload page**. Do **NOT** drag the outer folder itself; drag its **contents**.
7. Scroll down and click **Commit changes**.
8. Click the **Actions** tab at the top of your repo.
9. Wait about **2 minutes** for the build to finish (green check mark).
10. Click the completed workflow run, then scroll down to **Artifacts** and download **mod-jar**.
11. Unzip it and copy the `.jar` file into your `.minecraft/mods/` folder.

Make sure you have [Fabric Loader](https://fabricmc.net/use/installer/) and the [Fabric API](https://modrinth.com/mod/fabric-api) installed for Minecraft 1.20.1.

## Spawning the boss

Use a command to spawn the Stone Colossus:

```
/summon stonecolossus:stone_colossus
```

## License

MIT
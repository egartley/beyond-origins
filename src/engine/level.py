from pygame import Surface

from src.engine.camera import LevelCamera
from src.engine.game_state import GameState
from src.engine.level_entity import LevelEntity


class Level:

    TILE_SIZE = 32

    def __init__(self, data_dir: str, game_state: GameState):
        self.data_dir = data_dir
        self.gs = game_state
        self.rows, self.columns = 0, 0
        self.tiles = set()
        self.surface = None
        self.player = LevelEntity()
        self.camera = LevelCamera(self.player, 0, 0, 0, 0)
        self.start_rel_x, self.start_rel_y = 100, 100
        self.screen_width = game_state.screen.get_width()
        self.screen_height = game_state.screen.get_height()

    def _set_meta(self):
        with open("res/data/level/" + self.data_dir + "/meta.dat") as file:
            for i, line in enumerate(file):
                line = line.strip()
                if i == 0:
                    self.columns = int(line)
                elif i == 1:
                    self.rows = int(line)

    def _get_tile(self, tile_id: int) -> Surface:
        return self.gs.images.get(f"res/images/tiles/{tile_id}.png")

    def _build_tiles(self) -> set:
        tiles = set()
        with open("res/data/level/" + self.data_dir + "/tiles.dat") as file:
            lines = file.readlines()
            if len(lines) == 1:
                tile_id = int(lines[0])
                for r in range(self.rows):
                    for c in range(self.columns):
                        tiles.add((self._get_tile(tile_id), r, c))
            elif len(lines) == self.rows:
                for r, line in enumerate(lines):
                    tile_ids = [int(x) for x in line.strip().split(",")]
                    if len(tile_ids) == 1:
                        tile = self._get_tile(tile_ids[0])
                        for c in range(self.columns):
                            tiles.add((tile, r, c))
                    elif len(tile_ids) == self.columns:
                        for c in range(self.columns):
                            tiles.add((self._get_tile(tile_ids[c]), r, c))
                    else:
                        raise ValueError(f"Invalid tiles.dat (row {r} tile count does not match meta)")
            else:
                raise ValueError("Invalid tiles.dat (row count does not match meta)")
        return tiles

    def _get_tile_surface(self) -> Surface:
        surface = Surface((Level.TILE_SIZE * self.columns, Level.TILE_SIZE * self.rows))
        surface.convert()
        surface.blits([(tile[0], (tile[2] * Level.TILE_SIZE, tile[1] * Level.TILE_SIZE)) for tile in self.tiles])
        return surface

    def load(self):
        self._set_meta()
        self.tiles = self._build_tiles()
        self.surface = self._get_tile_surface()
        self.camera = LevelCamera(self.player, self.surface.get_width(), self.surface.get_height(),
                                  self.screen_width, self.screen_height)
        self.set_player_rel_position(self.start_rel_x, self.start_rel_y)

    def set_player_rel_position(self, x: float, y: float):
        lw = self.surface.get_width()
        lh = self.surface.get_height()
        x = min(x, lw - self.player.rect.width)
        y = min(y, lh - self.player.rect.height)
        csx, csy = self.screen_width // 2, self.screen_height // 2
        cew, ceh = self.player.rect.width // 2, self.player.rect.height // 2
        topleft_x = x + cew < csx
        topleft_y = y + ceh < csy
        set_x = x if topleft_x else (csx - cew)
        set_y = y if topleft_y else (csy - ceh)
        self.player.rel_x = x
        self.player.rel_y = y
        if not topleft_x:
            self.camera.view_x = (x + cew) - csx
        if not topleft_y:
            self.camera.view_y = (y + ceh) - csy
        max_vpx, max_vpy = lw - self.screen_width, lh - self.screen_height
        self.camera.view_x = min(self.camera.view_x, max_vpx)
        self.camera.view_y = min(self.camera.view_y, max_vpy)
        set_x += max(0, self.camera.view_x - max_vpx)
        set_y += max(0, self.camera.view_y - max_vpy)
        self.player.set_position(set_x, set_y)

    def tick(self, delta: float):
        self.player.tick(delta)
        self.camera.tick(delta)
        self.player.rel_x = self.camera.view_x + self.player.x
        self.player.rel_y = self.camera.view_y + self.player.y

    def render(self, surface: Surface):
        surface.blit(self.surface, (0, 0),
                     (self.camera.view_x, self.camera.view_y, self.screen_width, self.screen_height))
        self.player.render(surface)

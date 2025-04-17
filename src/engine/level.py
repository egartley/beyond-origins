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
        lw, lh = self.surface.get_size()
        max_x = lw - self.player.rect.width
        max_y = lh - self.player.rect.height
        x, y = max(0.0, min(x, max_x)), max(0.0, min(y, max_y))
        max_vpx, max_vpy = lw - self.screen_width, lh - self.screen_height
        screen_center_x, screen_center_y = self.screen_width // 2, self.screen_height // 2
        player_half_width, player_half_height = self.player.rect.width // 2, self.player.rect.height // 2

        at_left = x + player_half_width < screen_center_x
        at_top = y + player_half_height < screen_center_y
        abs_x = x if at_left else screen_center_x - player_half_width
        abs_y = y if at_top else screen_center_y - player_half_height

        self.camera.view_x = min(0 if at_left else (x + player_half_width) - screen_center_x, max_vpx)
        self.camera.view_y = min(0 if at_top else (y + player_half_height) - screen_center_y, max_vpy)
        relative_x = self.camera.view_x + abs_x
        relative_y = self.camera.view_y + abs_y
        abs_x += x - relative_x if relative_x != x else 0
        abs_y += y - relative_y if relative_y != y else 0

        self.player.set_position(abs_x, abs_y)
        self.player.rel_x, self.player.rel_y = x, y

    def tick(self, delta: float):
        self.player.tick(delta)
        self.camera.tick(delta)
        self.player.rel_x = self.camera.view_x + self.player.x
        self.player.rel_y = self.camera.view_y + self.player.y

    def render(self, surface: Surface):
        surface.blit(self.surface, (0, 0),
                     (self.camera.view_x, self.camera.view_y, self.screen_width, self.screen_height))
        self.player.render(surface)

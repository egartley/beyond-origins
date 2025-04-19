from pygame import Surface

from src.engine.game_state import GameState


class Level:

    TILE_SIZE = 32

    def __init__(self, data_dir: str, game_state: GameState):
        self.data_dir = data_dir
        self.gs = game_state
        self.rows, self.columns = 0, 0
        self.tiles = set()
        self.surface, self.tile_surface = None, None
        self.player = None
        self.cam_x, self.cam_y = 0, 0
        self.player_start_x, self.player_start_y = 100, 100
        self.screen_width, self.screen_height = game_state.screen.get_size()

    def _set_meta(self):
        with open("res/data/level/" + self.data_dir + "/meta.dat") as file:
            for i, line in enumerate(file):
                line = line.strip()
                if i == 0:
                    self.columns = int(line)
                elif i == 1:
                    self.rows = int(line)

    def _get_tile(self, tile_id: int, r: int, c: int) -> tuple[Surface, tuple[int, int]]:
        s = self.gs.images.get(f"res/images/tiles/{tile_id}.png")
        return s, (Level.TILE_SIZE * c, Level.TILE_SIZE * r)

    def _build_tiles(self):
        self.tiles = set()
        with open("res/data/level/" + self.data_dir + "/tiles.dat") as file:
            lines = file.readlines()
            if len(lines) == 1:
                tile_id = int(lines[0])
                for r in range(self.rows):
                    for c in range(self.columns):
                        self.tiles.add(self._get_tile(tile_id, r, c))
            elif len(lines) == self.rows:
                for r, line in enumerate(lines):
                    tile_ids = [int(x) for x in line.strip().split(",")]
                    for c in range(self.columns):
                        self.tiles.add(self._get_tile(tile_ids[0 if len(tile_ids) == 1 else c], r, c))
                    if len(tile_ids) != 1 and len(tile_ids) != self.columns:
                        raise ValueError(f"Invalid tiles.dat (row {r} tile count does not match meta)")
            else:
                raise ValueError("Invalid tiles.dat (row count does not match meta)")

    def _set_tile_surface(self):
        self.tile_surface = Surface((Level.TILE_SIZE * self.columns, Level.TILE_SIZE * self.rows))
        self.tile_surface.convert()
        self.tile_surface.blits([tile for tile in self.tiles])

    def load(self):
        self._set_meta()
        self._build_tiles()
        self._set_tile_surface()
        self.surface = Surface((self.tile_surface.get_size()))
        self.surface.convert()
        self.player.set_position(self.player_start_x, self.player_start_y)

    def tick(self, delta: float):
        self.player.tick(delta)
        cx = self.player.x - (self.screen_width // 2) + (self.player.rect.width // 2)
        cy = self.player.y - (self.screen_height // 2) + (self.player.rect.height // 2)
        self.cam_x = max(0, min(cx, self.surface.get_width() - self.screen_width))
        self.cam_y = max(0, min(cy, self.surface.get_height() - self.screen_height))

    def render(self, surface: Surface):
        viewport = (self.cam_x, self.cam_y, self.screen_width, self.screen_height)
        self.surface.blit(self.tile_surface, (viewport[0], viewport[1]), viewport)
        self.player.render(self.surface)
        surface.blit(self.surface, (0, 0), viewport)

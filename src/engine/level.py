from pygame import Surface

from src.engine.camera import Camera
from src.engine.game_state import GameState


class Level:

    TILE_SIZE = 32

    def __init__(self, data_dir: str, game_state: GameState):
        self.data_dir = data_dir
        self.gs = game_state
        self.rows, self.columns = 0, 0
        self.ground_tiles = set()
        self.ground_tile_surface = None
        self.camera = None
        self.control_entity = None

    def _read_meta(self):
        with open("res/data/level/" + self.data_dir + "/meta.dat") as file:
            for i, line in enumerate(file):
                line = line.strip()
                if i == 0:
                    self.columns = int(line)
                elif i == 1:
                    self.rows = int(line)

    def _get_tile(self, tile_id: int) -> Surface:
        return self.gs.images.get(f"res/images/tiles/{tile_id}.png")

    def _build_tiles(self, ):
        with open("res/data/level/" + self.data_dir + "/tiles.dat") as file:
            lines = file.readlines()
            if len(lines) == 1:
                tile_id = int(lines[0])
                for r in range(self.rows):
                    for c in range(self.columns):
                        self.ground_tiles.add((self._get_tile(tile_id), r, c))
            elif len(lines) == self.rows:
                for r, line in enumerate(file):
                    tiles = [int(x) for x in line.strip().split(",")]
                    if len(tiles) == 1:
                        tile = self._get_tile(tiles[0])
                        for c in range(self.columns):
                            self.ground_tiles.add((tile, r, c))
                    elif len(tiles) == self.columns:
                        for c in range(self.columns):
                            self.ground_tiles.add((self._get_tile(tiles[c]), r, c))
                    else:
                        raise ValueError(f"Invalid tiles.dat (row {r} tile count does not match meta)")
            else:
                raise ValueError("Invalid tiles.dat (row count does not match meta)")

    def _get_ground_tiles(self) -> Surface:
        surface = Surface((Level.TILE_SIZE * self.columns, Level.TILE_SIZE * self.rows))
        surface.convert()
        surface.blits([(gt[0], (gt[2] * Level.TILE_SIZE, gt[1] * Level.TILE_SIZE)) for gt in self.ground_tiles])
        return surface

    def load(self):
        try:
            self._read_meta()
            self._build_tiles()
        except Exception:
            raise ValueError(f"Invalid data for level \"{self.data_dir}\"")
        self.ground_tile_surface = self._get_ground_tiles()
        self.camera = Camera(self.ground_tile_surface.get_width(), self.ground_tile_surface.get_height(),
                             self.gs.screen.get_width(), self.gs.screen.get_height())

    def set_control_position(self, x: float, y: float):
        pass

    def tick(self, delta: float):
        self.control_entity.tick(delta)
        if any([self.control_entity.up, self.control_entity.down,
                self.control_entity.left, self.control_entity.right]):
            self.camera.tick(delta, self.control_entity)

    def render(self, surface: Surface):
        surface.blit(self.ground_tile_surface, (0, 0),
                     (self.camera.x, self.camera.y, surface.get_width(), surface.get_height()))
        self.control_entity.render(surface)

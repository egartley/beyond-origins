from pygame import Surface, Rect

from src.engine.game_state import GameState


TILE_SIZE = 32


class Level:

    def __init__(self, data_dir: str, game_state: GameState):
        self.data_dir = data_dir
        self.game_state = game_state
        self.surface = Surface((0, 0))
        self.surface.convert()
        self.rows, self.columns = 0, 0
        self.ground_tiles = set()

    def load(self):
        try:
            self.read_meta()
            self.build_tiles()
        except Exception:
            raise ValueError(f"Invalid data for level \"{self.data_dir}\"")
        self.surface = self.get_ground_tiles()

    def read_meta(self):
        with open("res/data/level/" + self.data_dir + "/meta.dat") as file:
            for i, line in enumerate(file):
                line = line.strip()
                if i == 0:
                    self.columns = int(line)
                elif i == 1:
                    self.rows = int(line)

    def get_tile(self, tile_id: int) -> Surface:
        return self.game_state.images.get(f"res/images/tiles/{tile_id}.png")

    def build_tiles(self,):
        with open("res/data/level/" + self.data_dir + "/tiles.dat") as file:
            lines = file.readlines()
            if len(lines) == 1:
                tile_id = int(lines[0])
                for r in range(self.rows):
                    for c in range(self.columns):
                        self.ground_tiles.add((self.get_tile(tile_id), r, c))
            elif len(lines) == self.rows:
                for r, line in enumerate(file):
                    tiles = [int(x) for x in line.strip().split(",")]
                    if len(tiles) == 1:
                        tile = self.get_tile(tiles[0])
                        for c in range(self.columns):
                            self.ground_tiles.add((tile, r, c))
                    elif len(tiles) == self.columns:
                        for c in range(self.columns):
                            self.ground_tiles.add((self.get_tile(tiles[c]), r, c))
                    else:
                        raise ValueError(f"Invalid tiles.dat (row {r} tile count does not match meta)")
            else:
                raise ValueError("Invalid tiles.dat (row count does not match meta)")

    def get_ground_tiles(self) -> Surface:
        surface = Surface((TILE_SIZE * self.columns, TILE_SIZE * self.rows))
        blits = []
        for gt in self.ground_tiles:
            blits.append((gt[0], (gt[2] * TILE_SIZE, gt[1] * TILE_SIZE)))
        surface.blits(blits)
        return surface

    def tick(self, delta: float):
        pass

    def render(self, surface: Surface) -> list[Rect | None]:
        surface.blit(self.surface, (0, 0))
        return [None]

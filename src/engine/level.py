from pygame import Surface

from src.engine.camera import Camera
from src.engine.game_state import GameState
from src.engine.level_entity import LevelEntity


class Level:

    TILE_SIZE = 32

    def __init__(self, data_dir: str, game_state: GameState):
        self.data_dir = data_dir
        self.gs = game_state
        self.rows, self.columns = 0, 0
        self.start_rel_x, self.start_rel_y = 100, 100
        self.ground_tiles = set()
        self.ground_tile_surface = None
        self.camera = Camera(0, 0, 0, 0)
        self.control_entity = LevelEntity()

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
                for r, line in enumerate(lines):
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
        self.set_control_entity_rel_position(self.start_rel_x, self.start_rel_y)

    def set_control_entity(self, e: LevelEntity):
        self.control_entity = e

    def set_control_entity_rel_position(self, x: float, y: float):
        lw = self.ground_tile_surface.get_width()
        lh = self.ground_tile_surface.get_height()
        sw, sh = self.gs.screen.get_width(), self.gs.screen.get_height()
        max_rx = lw - self.control_entity.rect.width
        max_ry = lh - self.control_entity.rect.height
        if x > max_rx:
            x = max_rx
        if y > max_ry:
            y = max_ry
        csx, csy = sw // 2, sh // 2
        cew = self.control_entity.rect.width // 2
        ceh = self.control_entity.rect.height // 2
        topleft_x, topleft_y = csx > (x + cew), csy > (y + ceh)
        set_x = x if topleft_x else (csx - cew)
        set_y = y if topleft_y else (csy - ceh)
        self.control_entity.rel_x = x
        self.control_entity.rel_y = y
        if not topleft_x:
            self.camera.viewport_x = (x + cew) - csx
        if not topleft_y:
            self.camera.viewport_y = (y + ceh) - csy
        max_vpx, max_vpy = lw - sw, lh - sh
        corrected_x, corrected_y = set_x, set_y
        if self.camera.viewport_x > max_vpx:
            corrected_x += self.camera.viewport_x - max_vpx
            self.camera.viewport_x = max_vpx
        if self.camera.viewport_y > max_vpy:
            corrected_y += self.camera.viewport_y - max_vpy
            self.camera.viewport_y = max_vpy
        self.control_entity.set_position(corrected_x, corrected_y)

    def tick(self, delta: float):
        self.control_entity.tick(delta)
        self.camera.tick(delta, self.control_entity)
        self.control_entity.rel_x = self.camera.viewport_x + self.control_entity.x
        self.control_entity.rel_y = self.camera.viewport_y + self.control_entity.y

    def render(self, surface: Surface):
        surface.blit(self.ground_tile_surface, (0, 0),
                     (self.camera.viewport_x, self.camera.viewport_y, surface.get_width(), surface.get_height()))
        self.control_entity.render(surface)

from src.engine.level_entity import LevelEntity


class Camera:

    def __init__(self, lw: int, lh: int, sw: int, sh: int):
        self.lw, self.lh, self.sw, self.sh = lw, lh, sw, sh
        self.x, self.y = 0, 0
        self.center_x = self.sw // 2
        self.center_y = self.sh // 2

    def tick(self, delta: float, ce: LevelEntity):
        cc = ce.speed * delta
        cx = abs(ce.rect.centerx - self.center_x) <= cc
        cy = abs(ce.rect.centery - self.center_y) <= cc
        left = self.x <= 0
        right = (self.x + self.sw) >= self.lw
        top = self.y <= 0
        bottom = (self.y + self.sh) >= self.lh

        if cx:
            if ce.left:
                if left:
                    ce.move_x(-ce.speed, delta)
                else:
                    self.x -= cc
            if ce.right:
                if right:
                    ce.move_x(ce.speed, delta)
                else:
                    self.x += cc
            if ce.up and not cy:
                ce.move_y(-ce.speed, delta)
            if ce.down and not cy:
                ce.move_y(ce.speed, delta)
        if cy:
            if ce.up:
                if top:
                    ce.move_y(-ce.speed, delta)
                else:
                    self.y -= cc
            if ce.down:
                if bottom:
                    ce.move_y(ce.speed, delta)
                else:
                    self.y += cc
            if ce.left and not cx:
                ce.move_x(-ce.speed, delta)
            if ce.right and not cx:
                ce.move_x(ce.speed, delta)
        if not cx and not cy:
            if ce.up:
                ce.move_y(-ce.speed, delta)
            elif ce.down:
                ce.move_y(ce.speed, delta)
            if ce.left:
                ce.move_x(-ce.speed, delta)
            elif ce.right:
                ce.move_x(ce.speed, delta)

        if self.x < 0:
            self.x = 0
        if self.x + self.sw > self.lw:
            self.x = self.lw - self.sw
        if self.y < 0:
            self.y = 0
        if self.y + self.sh > self.lh:
            self.y = self.lh - self.sh

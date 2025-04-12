from src.engine.level_entity import LevelEntity


class Camera:

    def __init__(self, lw: int, lh: int, sw: int, sh: int):
        self.lw, self.lh, self.sw, self.sh = lw, lh, sw, sh
        self.viewport_x, self.viewport_y = 0, 0

    def tick(self, delta: float, ce: LevelEntity):
        cc_x = ce.vel_x * delta
        cc_y = ce.vel_y * delta
        cx = abs(ce.rect.centerx - (self.sw // 2)) <= abs(cc_x)
        cy = abs(ce.rect.centery - (self.sh // 2)) <= abs(cc_y)
        bound_left = self.viewport_x <= 0
        bound_right = (self.viewport_x + self.sw) >= self.lw
        bound_top = self.viewport_y <= 0
        bound_bottom = (self.viewport_y + self.sh) >= self.lh

        move_x, move_y = cx, cy
        if cx:
            if ce.left:
                move_x = not bound_left
            if ce.right:
                move_x = not bound_right
            if (ce.up or ce.down) and not cy:
                move_x = False
        if cy:
            if ce.up:
                move_y = not bound_top
            if ce.down:
                move_y = not bound_bottom
            if (ce.left or ce.right) and not cx:
                move_y = False

        if move_x:
            self.viewport_x += cc_x
        else:
            ce.move_x(delta)
        if move_y:
            self.viewport_y += cc_y
        else:
            ce.move_y(delta)

        if self.viewport_x < 0:
            self.viewport_x = 0
        if self.viewport_x + self.sw > self.lw:
            self.viewport_x = self.lw - self.sw
        if self.viewport_y < 0:
            self.viewport_y = 0
        if self.viewport_y + self.sh > self.lh:
            self.viewport_y = self.lh - self.sh

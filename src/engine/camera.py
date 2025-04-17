from src.engine.level_entity import LevelEntity


class LevelCamera:

    def __init__(self, player: LevelEntity, lw: int, lh: int, sw: int, sh: int):
        self.player = player
        self.lw, self.lh, self.sw, self.sh = lw, lh, sw, sh
        self.view_x, self.view_y = 0, 0

    def tick(self, delta: float):
        cc_x = self.player.vel_x * delta
        cc_y = self.player.vel_y * delta
        cx = abs(self.player.rect.centerx - (self.sw // 2)) <= abs(cc_x)
        cy = abs(self.player.rect.centery - (self.sh // 2)) <= abs(cc_y)
        bound_left = self.view_x <= 0
        bound_right = (self.view_x + self.sw) >= self.lw
        bound_top = self.view_y <= 0
        bound_bottom = (self.view_y + self.sh) >= self.lh

        move_x, move_y = cx, cy
        if cx:
            if self.player.left:
                move_x = not bound_left
            if self.player.right:
                move_x = not bound_right
            if not cy and (self.player.up or self.player.down):
                move_y = False
        if cy:
            if self.player.up:
                move_y = not bound_top
            if self.player.down:
                move_y = not bound_bottom
            if not cx and (self.player.left or self.player.right):
                move_x = False
        if not cx and not cy:
            if self.player.up or self.player.down:
                move_y = False
            if self.player.left or self.player.right:
                move_x = False

        if move_x:
            self.view_x += cc_x
        else:
            self.player.move_x(delta)
        if move_y:
            self.view_y += cc_y
        else:
            self.player.move_y(delta)

        if self.view_x < 0:
            self.view_x = 0
        if self.view_x + self.sw > self.lw:
            self.view_x = self.lw - self.sw
        if self.view_y < 0:
            self.view_y = 0
        if self.view_y + self.sh > self.lh:
            self.view_y = self.lh - self.sh

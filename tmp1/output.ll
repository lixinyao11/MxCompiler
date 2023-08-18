declare void @print(ptr)
declare ptr @_string_concat(ptr, ptr)
declare ptr @_string_copy(ptr)
declare i32 @_string_compare(ptr, ptr)
declare ptr @_malloc_array(i32, i32)
declare void @println(ptr)
declare void @printInt(i32)
declare void @printlnInt(i32)
declare ptr @getString()
declare i32 @getInt()
declare ptr @toString(i32)
declare i32 @_string_length(ptr)
declare ptr @_string_substring(ptr, i32, i32)
declare i32 @_string_parseInt(ptr)
declare i32 @_string_ord(ptr, i32)
@string.0 = constant [15 x i8] c"no solution!\\n\00"
define i32 @main() {
entry:
  %N.1 = alloca i32
  %head.1 = alloca i32
  %startx.1 = alloca i32
  %starty.1 = alloca i32
  %targetx.1 = alloca i32
  %targety.1 = alloca i32
  %tail.1 = alloca i32
  %ok.1 = alloca i32
  %now.1 = alloca i32
  %x.1 = alloca i32
  %y.1 = alloca i32
  %xlist.1 = alloca ptr
  %ylist.1 = alloca ptr
  %step.1 = alloca ptr
  %i.1 = alloca i32
  %j.1 = alloca i32
  %_0 = load i32, ptr %N.1
  %_1 = call i32 @getInt()
  store i32 %_1, ptr %N.1
  %_2 = load i32, ptr %head.1
  store i32 0, ptr %head.1
  %_3 = load i32, ptr %tail.1
  store i32 0, ptr %tail.1
  %_4 = load i32, ptr %startx.1
  store i32 0, ptr %startx.1
  %_5 = load i32, ptr %starty.1
  store i32 0, ptr %starty.1
  %_6 = load i32, ptr %targetx.1
  %_7 = load i32, ptr %N.1
  %_8 = sub i32 %_7, 1
  store i32 %_8, ptr %targetx.1
  %_9 = load i32, ptr %targety.1
  %_10 = load i32, ptr %N.1
  %_11 = sub i32 %_10, 1
  store i32 %_11, ptr %targety.1
  %_12 = load i32, ptr %x.1
  store i32 0, ptr %x.1
  %_13 = load i32, ptr %y.1
  store i32 0, ptr %y.1
  %_14 = load i32, ptr %now.1
  store i32 0, ptr %now.1
  %_15 = load i32, ptr %ok.1
  store i32 0, ptr %ok.1
  %_16 = load ptr, ptr %xlist.1
  %_17 = load i32, ptr %N.1
  %_18 = load i32, ptr %N.1
  %_19 = mul i32 %_17, %_18
  %_20 = call ptr @_malloc_array(i32 4, i32 %_19)
  store ptr %_20, ptr %xlist.1
  %_21 = load i32, ptr %i.1
  store i32 0, ptr %i.1
  br label %for.cond.0
for.cond.0:
  %_22 = load i32, ptr %i.1
  %_23 = load i32, ptr %N.1
  %_24 = load i32, ptr %N.1
  %_25 = mul i32 %_23, %_24
  %_26 = icmp slt i32 %_22, %_25
  br i1 %_26, label %for.body.0, label %for.end.0
for.body.0:
  %_27 = load ptr, ptr %xlist.1
  %_28 = load i32, ptr %i.1
  %_29 = getelementptr i32, ptr %_27, i32 %_28
  %_30 = load i32, ptr %_29
  store i32 0, ptr %_29
  br label %for.step.0
for.step.0:
  %_31 = load i32, ptr %i.1
  %_32 = add i32 %_31, 1
  store i32 %_32, ptr %i.1
  br label %for.cond.0
for.end.0:
  %_33 = load ptr, ptr %ylist.1
  %_34 = load i32, ptr %N.1
  %_35 = load i32, ptr %N.1
  %_36 = mul i32 %_34, %_35
  %_37 = call ptr @_malloc_array(i32 4, i32 %_36)
  store ptr %_37, ptr %ylist.1
  %_38 = load i32, ptr %i.1
  store i32 0, ptr %i.1
  br label %for.cond.1
for.cond.1:
  %_39 = load i32, ptr %i.1
  %_40 = load i32, ptr %N.1
  %_41 = load i32, ptr %N.1
  %_42 = mul i32 %_40, %_41
  %_43 = icmp slt i32 %_39, %_42
  br i1 %_43, label %for.body.1, label %for.end.1
for.body.1:
  %_44 = load ptr, ptr %ylist.1
  %_45 = load i32, ptr %i.1
  %_46 = getelementptr i32, ptr %_44, i32 %_45
  %_47 = load i32, ptr %_46
  store i32 0, ptr %_46
  br label %for.step.1
for.step.1:
  %_48 = load i32, ptr %i.1
  %_49 = add i32 %_48, 1
  store i32 %_49, ptr %i.1
  br label %for.cond.1
for.end.1:
  %_50 = load ptr, ptr %step.1
  %_51 = load i32, ptr %N.1
  %_52 = call ptr @_malloc_array(i32 8, i32 %_51)
  store ptr %_52, ptr %step.1
  %_53 = load i32, ptr %i.1
  store i32 0, ptr %i.1
  br label %for.cond.2
for.cond.2:
  %_54 = load i32, ptr %i.1
  %_55 = load i32, ptr %N.1
  %_56 = icmp slt i32 %_54, %_55
  br i1 %_56, label %for.body.2, label %for.end.2
for.body.2:
  %_57 = load ptr, ptr %step.1
  %_58 = load i32, ptr %i.1
  %_59 = getelementptr ptr, ptr %_57, i32 %_58
  %_60 = load ptr, ptr %_59
  %_61 = load i32, ptr %N.1
  %_62 = call ptr @_malloc_array(i32 4, i32 %_61)
  store ptr %_62, ptr %_59
  %_63 = load i32, ptr %j.1
  store i32 0, ptr %j.1
  br label %for.cond.3
for.cond.3:
  %_64 = load i32, ptr %j.1
  %_65 = load i32, ptr %N.1
  %_66 = icmp slt i32 %_64, %_65
  br i1 %_66, label %for.body.3, label %for.end.3
for.body.3:
  %_67 = load ptr, ptr %step.1
  %_68 = load i32, ptr %i.1
  %_69 = getelementptr ptr, ptr %_67, i32 %_68
  %_70 = load ptr, ptr %_69
  %_71 = load i32, ptr %j.1
  %_72 = getelementptr i32, ptr %_70, i32 %_71
  %_73 = load i32, ptr %_72
  %_74 = sub i32 0, 1
  store i32 %_74, ptr %_72
  br label %for.step.3
for.step.3:
  %_75 = load i32, ptr %j.1
  %_76 = add i32 %_75, 1
  store i32 %_76, ptr %j.1
  br label %for.cond.3
for.end.3:
  br label %for.step.2
for.step.2:
  %_77 = load i32, ptr %i.1
  %_78 = add i32 %_77, 1
  store i32 %_78, ptr %i.1
  br label %for.cond.2
for.end.2:
  %_79 = load ptr, ptr %xlist.1
  %_80 = getelementptr i32, ptr %_79, i32 0
  %_81 = load i32, ptr %_80
  %_82 = load i32, ptr %startx.1
  store i32 %_82, ptr %_80
  %_83 = load ptr, ptr %ylist.1
  %_84 = getelementptr i32, ptr %_83, i32 0
  %_85 = load i32, ptr %_84
  %_86 = load i32, ptr %starty.1
  store i32 %_86, ptr %_84
  %_87 = load ptr, ptr %step.1
  %_88 = load i32, ptr %startx.1
  %_89 = getelementptr ptr, ptr %_87, i32 %_88
  %_90 = load ptr, ptr %_89
  %_91 = load i32, ptr %starty.1
  %_92 = getelementptr i32, ptr %_90, i32 %_91
  %_93 = load i32, ptr %_92
  store i32 0, ptr %_92
  br label %while.cond.0
while.cond.0:
  %_94 = load i32, ptr %head.1
  %_95 = load i32, ptr %tail.1
  %_96 = icmp sle i32 %_94, %_95
  br i1 %_96, label %while.body.0, label %while.end.0
while.body.0:
  %_97 = load i32, ptr %now.1
  %_98 = load ptr, ptr %step.1
  %_99 = load ptr, ptr %xlist.1
  %_100 = load i32, ptr %head.1
  %_101 = getelementptr i32, ptr %_99, i32 %_100
  %_102 = load i32, ptr %_101
  %_103 = getelementptr ptr, ptr %_98, i32 %_102
  %_104 = load ptr, ptr %_103
  %_105 = load ptr, ptr %ylist.1
  %_106 = load i32, ptr %head.1
  %_107 = getelementptr i32, ptr %_105, i32 %_106
  %_108 = load i32, ptr %_107
  %_109 = getelementptr i32, ptr %_104, i32 %_108
  %_110 = load i32, ptr %_109
  store i32 %_110, ptr %now.1
  %_111 = load i32, ptr %x.1
  %_112 = load ptr, ptr %xlist.1
  %_113 = load i32, ptr %head.1
  %_114 = getelementptr i32, ptr %_112, i32 %_113
  %_115 = load i32, ptr %_114
  %_116 = sub i32 %_115, 1
  store i32 %_116, ptr %x.1
  %_117 = load i32, ptr %y.1
  %_118 = load ptr, ptr %ylist.1
  %_119 = load i32, ptr %head.1
  %_120 = getelementptr i32, ptr %_118, i32 %_119
  %_121 = load i32, ptr %_120
  %_122 = sub i32 %_121, 2
  store i32 %_122, ptr %y.1
  %_124 = load i32, ptr %x.1
  %_125 = load i32, ptr %N.1
  %_123 = call i1 @check(i32 %_124, i32 %_125)
  %_126 = alloca i1
  br i1 %_123, label %if.then.0, label %if.else.0
if.then.0:
  %_128 = load i32, ptr %y.1
  %_129 = load i32, ptr %N.1
  %_127 = call i1 @check(i32 %_128, i32 %_129)
  store i1 %_127, ptr %_126
  br label %if.end.0
if.else.0:
  store i1 %_123, ptr %_126
  br label %if.end.0
if.end.0:
  %_130 = load i1, ptr %_126
  %_131 = alloca i1
  br i1 %_130, label %if.then.1, label %if.else.1
if.then.1:
  %_132 = load ptr, ptr %step.1
  %_133 = load i32, ptr %x.1
  %_134 = getelementptr ptr, ptr %_132, i32 %_133
  %_135 = load ptr, ptr %_134
  %_136 = load i32, ptr %y.1
  %_137 = getelementptr i32, ptr %_135, i32 %_136
  %_138 = load i32, ptr %_137
  %_139 = sub i32 0, 1
  %_140 = icmp eq i32 %_138, %_139
  store i1 %_140, ptr %_131
  br label %if.end.1
if.else.1:
  store i1 %_130, ptr %_131
  br label %if.end.1
if.end.1:
  %_141 = load i1, ptr %_131
  br i1 %_141, label %if.then.2, label %if.end.2
if.then.2:
  %_142 = load i32, ptr %tail.1
  %_143 = load i32, ptr %tail.1
  %_144 = add i32 %_143, 1
  store i32 %_144, ptr %tail.1
  %_145 = load ptr, ptr %xlist.1
  %_146 = load i32, ptr %tail.1
  %_147 = getelementptr i32, ptr %_145, i32 %_146
  %_148 = load i32, ptr %_147
  %_149 = load i32, ptr %x.1
  store i32 %_149, ptr %_147
  %_150 = load ptr, ptr %ylist.1
  %_151 = load i32, ptr %tail.1
  %_152 = getelementptr i32, ptr %_150, i32 %_151
  %_153 = load i32, ptr %_152
  %_154 = load i32, ptr %y.1
  store i32 %_154, ptr %_152
  %_155 = load ptr, ptr %step.1
  %_156 = load i32, ptr %x.1
  %_157 = getelementptr ptr, ptr %_155, i32 %_156
  %_158 = load ptr, ptr %_157
  %_159 = load i32, ptr %y.1
  %_160 = getelementptr i32, ptr %_158, i32 %_159
  %_161 = load i32, ptr %_160
  %_162 = load i32, ptr %now.1
  %_163 = add i32 %_162, 1
  store i32 %_163, ptr %_160
  %_164 = load i32, ptr %x.1
  %_165 = load i32, ptr %targetx.1
  %_166 = icmp eq i32 %_164, %_165
  %_167 = alloca i1
  br i1 %_166, label %if.then.3, label %if.else.3
if.then.3:
  %_168 = load i32, ptr %y.1
  %_169 = load i32, ptr %targety.1
  %_170 = icmp eq i32 %_168, %_169
  store i1 %_170, ptr %_167
  br label %if.end.3
if.else.3:
  store i1 %_166, ptr %_167
  br label %if.end.3
if.end.3:
  %_171 = load i1, ptr %_167
  br i1 %_171, label %if.then.4, label %if.end.4
if.then.4:
  %_172 = load i32, ptr %ok.1
  store i32 1, ptr %ok.1
  br label %if.end.4
if.end.4:
  br label %if.end.2
if.end.2:
  %_173 = load i32, ptr %x.1
  %_174 = load ptr, ptr %xlist.1
  %_175 = load i32, ptr %head.1
  %_176 = getelementptr i32, ptr %_174, i32 %_175
  %_177 = load i32, ptr %_176
  %_178 = sub i32 %_177, 1
  store i32 %_178, ptr %x.1
  %_179 = load i32, ptr %y.1
  %_180 = load ptr, ptr %ylist.1
  %_181 = load i32, ptr %head.1
  %_182 = getelementptr i32, ptr %_180, i32 %_181
  %_183 = load i32, ptr %_182
  %_184 = add i32 %_183, 2
  store i32 %_184, ptr %y.1
  %_186 = load i32, ptr %x.1
  %_187 = load i32, ptr %N.1
  %_185 = call i1 @check(i32 %_186, i32 %_187)
  %_188 = alloca i1
  br i1 %_185, label %if.then.5, label %if.else.5
if.then.5:
  %_190 = load i32, ptr %y.1
  %_191 = load i32, ptr %N.1
  %_189 = call i1 @check(i32 %_190, i32 %_191)
  store i1 %_189, ptr %_188
  br label %if.end.5
if.else.5:
  store i1 %_185, ptr %_188
  br label %if.end.5
if.end.5:
  %_192 = load i1, ptr %_188
  %_193 = alloca i1
  br i1 %_192, label %if.then.6, label %if.else.6
if.then.6:
  %_194 = load ptr, ptr %step.1
  %_195 = load i32, ptr %x.1
  %_196 = getelementptr ptr, ptr %_194, i32 %_195
  %_197 = load ptr, ptr %_196
  %_198 = load i32, ptr %y.1
  %_199 = getelementptr i32, ptr %_197, i32 %_198
  %_200 = load i32, ptr %_199
  %_201 = sub i32 0, 1
  %_202 = icmp eq i32 %_200, %_201
  store i1 %_202, ptr %_193
  br label %if.end.6
if.else.6:
  store i1 %_192, ptr %_193
  br label %if.end.6
if.end.6:
  %_203 = load i1, ptr %_193
  br i1 %_203, label %if.then.7, label %if.end.7
if.then.7:
  %_204 = load i32, ptr %tail.1
  %_205 = load i32, ptr %tail.1
  %_206 = add i32 %_205, 1
  store i32 %_206, ptr %tail.1
  %_207 = load ptr, ptr %xlist.1
  %_208 = load i32, ptr %tail.1
  %_209 = getelementptr i32, ptr %_207, i32 %_208
  %_210 = load i32, ptr %_209
  %_211 = load i32, ptr %x.1
  store i32 %_211, ptr %_209
  %_212 = load ptr, ptr %ylist.1
  %_213 = load i32, ptr %tail.1
  %_214 = getelementptr i32, ptr %_212, i32 %_213
  %_215 = load i32, ptr %_214
  %_216 = load i32, ptr %y.1
  store i32 %_216, ptr %_214
  %_217 = load ptr, ptr %step.1
  %_218 = load i32, ptr %x.1
  %_219 = getelementptr ptr, ptr %_217, i32 %_218
  %_220 = load ptr, ptr %_219
  %_221 = load i32, ptr %y.1
  %_222 = getelementptr i32, ptr %_220, i32 %_221
  %_223 = load i32, ptr %_222
  %_224 = load i32, ptr %now.1
  %_225 = add i32 %_224, 1
  store i32 %_225, ptr %_222
  %_226 = load i32, ptr %x.1
  %_227 = load i32, ptr %targetx.1
  %_228 = icmp eq i32 %_226, %_227
  %_229 = alloca i1
  br i1 %_228, label %if.then.8, label %if.else.8
if.then.8:
  %_230 = load i32, ptr %y.1
  %_231 = load i32, ptr %targety.1
  %_232 = icmp eq i32 %_230, %_231
  store i1 %_232, ptr %_229
  br label %if.end.8
if.else.8:
  store i1 %_228, ptr %_229
  br label %if.end.8
if.end.8:
  %_233 = load i1, ptr %_229
  br i1 %_233, label %if.then.9, label %if.end.9
if.then.9:
  %_234 = load i32, ptr %ok.1
  store i32 1, ptr %ok.1
  br label %if.end.9
if.end.9:
  br label %if.end.7
if.end.7:
  %_235 = load i32, ptr %x.1
  %_236 = load ptr, ptr %xlist.1
  %_237 = load i32, ptr %head.1
  %_238 = getelementptr i32, ptr %_236, i32 %_237
  %_239 = load i32, ptr %_238
  %_240 = add i32 %_239, 1
  store i32 %_240, ptr %x.1
  %_241 = load i32, ptr %y.1
  %_242 = load ptr, ptr %ylist.1
  %_243 = load i32, ptr %head.1
  %_244 = getelementptr i32, ptr %_242, i32 %_243
  %_245 = load i32, ptr %_244
  %_246 = sub i32 %_245, 2
  store i32 %_246, ptr %y.1
  %_248 = load i32, ptr %x.1
  %_249 = load i32, ptr %N.1
  %_247 = call i1 @check(i32 %_248, i32 %_249)
  %_250 = alloca i1
  br i1 %_247, label %if.then.10, label %if.else.10
if.then.10:
  %_252 = load i32, ptr %y.1
  %_253 = load i32, ptr %N.1
  %_251 = call i1 @check(i32 %_252, i32 %_253)
  store i1 %_251, ptr %_250
  br label %if.end.10
if.else.10:
  store i1 %_247, ptr %_250
  br label %if.end.10
if.end.10:
  %_254 = load i1, ptr %_250
  %_255 = alloca i1
  br i1 %_254, label %if.then.11, label %if.else.11
if.then.11:
  %_256 = load ptr, ptr %step.1
  %_257 = load i32, ptr %x.1
  %_258 = getelementptr ptr, ptr %_256, i32 %_257
  %_259 = load ptr, ptr %_258
  %_260 = load i32, ptr %y.1
  %_261 = getelementptr i32, ptr %_259, i32 %_260
  %_262 = load i32, ptr %_261
  %_263 = sub i32 0, 1
  %_264 = icmp eq i32 %_262, %_263
  store i1 %_264, ptr %_255
  br label %if.end.11
if.else.11:
  store i1 %_254, ptr %_255
  br label %if.end.11
if.end.11:
  %_265 = load i1, ptr %_255
  br i1 %_265, label %if.then.12, label %if.end.12
if.then.12:
  %_266 = load i32, ptr %tail.1
  %_267 = load i32, ptr %tail.1
  %_268 = add i32 %_267, 1
  store i32 %_268, ptr %tail.1
  %_269 = load ptr, ptr %xlist.1
  %_270 = load i32, ptr %tail.1
  %_271 = getelementptr i32, ptr %_269, i32 %_270
  %_272 = load i32, ptr %_271
  %_273 = load i32, ptr %x.1
  store i32 %_273, ptr %_271
  %_274 = load ptr, ptr %ylist.1
  %_275 = load i32, ptr %tail.1
  %_276 = getelementptr i32, ptr %_274, i32 %_275
  %_277 = load i32, ptr %_276
  %_278 = load i32, ptr %y.1
  store i32 %_278, ptr %_276
  %_279 = load ptr, ptr %step.1
  %_280 = load i32, ptr %x.1
  %_281 = getelementptr ptr, ptr %_279, i32 %_280
  %_282 = load ptr, ptr %_281
  %_283 = load i32, ptr %y.1
  %_284 = getelementptr i32, ptr %_282, i32 %_283
  %_285 = load i32, ptr %_284
  %_286 = load i32, ptr %now.1
  %_287 = add i32 %_286, 1
  store i32 %_287, ptr %_284
  %_288 = load i32, ptr %x.1
  %_289 = load i32, ptr %targetx.1
  %_290 = icmp eq i32 %_288, %_289
  %_291 = alloca i1
  br i1 %_290, label %if.then.13, label %if.else.13
if.then.13:
  %_292 = load i32, ptr %y.1
  %_293 = load i32, ptr %targety.1
  %_294 = icmp eq i32 %_292, %_293
  store i1 %_294, ptr %_291
  br label %if.end.13
if.else.13:
  store i1 %_290, ptr %_291
  br label %if.end.13
if.end.13:
  %_295 = load i1, ptr %_291
  br i1 %_295, label %if.then.14, label %if.end.14
if.then.14:
  %_296 = load i32, ptr %ok.1
  store i32 1, ptr %ok.1
  br label %if.end.14
if.end.14:
  br label %if.end.12
if.end.12:
  %_297 = load i32, ptr %x.1
  %_298 = load ptr, ptr %xlist.1
  %_299 = load i32, ptr %head.1
  %_300 = getelementptr i32, ptr %_298, i32 %_299
  %_301 = load i32, ptr %_300
  %_302 = add i32 %_301, 1
  store i32 %_302, ptr %x.1
  %_303 = load i32, ptr %y.1
  %_304 = load ptr, ptr %ylist.1
  %_305 = load i32, ptr %head.1
  %_306 = getelementptr i32, ptr %_304, i32 %_305
  %_307 = load i32, ptr %_306
  %_308 = add i32 %_307, 2
  store i32 %_308, ptr %y.1
  %_310 = load i32, ptr %x.1
  %_311 = load i32, ptr %N.1
  %_309 = call i1 @check(i32 %_310, i32 %_311)
  %_312 = alloca i1
  br i1 %_309, label %if.then.15, label %if.else.15
if.then.15:
  %_314 = load i32, ptr %y.1
  %_315 = load i32, ptr %N.1
  %_313 = call i1 @check(i32 %_314, i32 %_315)
  store i1 %_313, ptr %_312
  br label %if.end.15
if.else.15:
  store i1 %_309, ptr %_312
  br label %if.end.15
if.end.15:
  %_316 = load i1, ptr %_312
  %_317 = alloca i1
  br i1 %_316, label %if.then.16, label %if.else.16
if.then.16:
  %_318 = load ptr, ptr %step.1
  %_319 = load i32, ptr %x.1
  %_320 = getelementptr ptr, ptr %_318, i32 %_319
  %_321 = load ptr, ptr %_320
  %_322 = load i32, ptr %y.1
  %_323 = getelementptr i32, ptr %_321, i32 %_322
  %_324 = load i32, ptr %_323
  %_325 = sub i32 0, 1
  %_326 = icmp eq i32 %_324, %_325
  store i1 %_326, ptr %_317
  br label %if.end.16
if.else.16:
  store i1 %_316, ptr %_317
  br label %if.end.16
if.end.16:
  %_327 = load i1, ptr %_317
  br i1 %_327, label %if.then.17, label %if.end.17
if.then.17:
  %_328 = load i32, ptr %tail.1
  %_329 = load i32, ptr %tail.1
  %_330 = add i32 %_329, 1
  store i32 %_330, ptr %tail.1
  %_331 = load ptr, ptr %xlist.1
  %_332 = load i32, ptr %tail.1
  %_333 = getelementptr i32, ptr %_331, i32 %_332
  %_334 = load i32, ptr %_333
  %_335 = load i32, ptr %x.1
  store i32 %_335, ptr %_333
  %_336 = load ptr, ptr %ylist.1
  %_337 = load i32, ptr %tail.1
  %_338 = getelementptr i32, ptr %_336, i32 %_337
  %_339 = load i32, ptr %_338
  %_340 = load i32, ptr %y.1
  store i32 %_340, ptr %_338
  %_341 = load ptr, ptr %step.1
  %_342 = load i32, ptr %x.1
  %_343 = getelementptr ptr, ptr %_341, i32 %_342
  %_344 = load ptr, ptr %_343
  %_345 = load i32, ptr %y.1
  %_346 = getelementptr i32, ptr %_344, i32 %_345
  %_347 = load i32, ptr %_346
  %_348 = load i32, ptr %now.1
  %_349 = add i32 %_348, 1
  store i32 %_349, ptr %_346
  %_350 = load i32, ptr %x.1
  %_351 = load i32, ptr %targetx.1
  %_352 = icmp eq i32 %_350, %_351
  %_353 = alloca i1
  br i1 %_352, label %if.then.18, label %if.else.18
if.then.18:
  %_354 = load i32, ptr %y.1
  %_355 = load i32, ptr %targety.1
  %_356 = icmp eq i32 %_354, %_355
  store i1 %_356, ptr %_353
  br label %if.end.18
if.else.18:
  store i1 %_352, ptr %_353
  br label %if.end.18
if.end.18:
  %_357 = load i1, ptr %_353
  br i1 %_357, label %if.then.19, label %if.end.19
if.then.19:
  %_358 = load i32, ptr %ok.1
  store i32 1, ptr %ok.1
  br label %if.end.19
if.end.19:
  br label %if.end.17
if.end.17:
  %_359 = load i32, ptr %x.1
  %_360 = load ptr, ptr %xlist.1
  %_361 = load i32, ptr %head.1
  %_362 = getelementptr i32, ptr %_360, i32 %_361
  %_363 = load i32, ptr %_362
  %_364 = sub i32 %_363, 2
  store i32 %_364, ptr %x.1
  %_365 = load i32, ptr %y.1
  %_366 = load ptr, ptr %ylist.1
  %_367 = load i32, ptr %head.1
  %_368 = getelementptr i32, ptr %_366, i32 %_367
  %_369 = load i32, ptr %_368
  %_370 = sub i32 %_369, 1
  store i32 %_370, ptr %y.1
  %_372 = load i32, ptr %x.1
  %_373 = load i32, ptr %N.1
  %_371 = call i1 @check(i32 %_372, i32 %_373)
  %_374 = alloca i1
  br i1 %_371, label %if.then.20, label %if.else.20
if.then.20:
  %_376 = load i32, ptr %y.1
  %_377 = load i32, ptr %N.1
  %_375 = call i1 @check(i32 %_376, i32 %_377)
  store i1 %_375, ptr %_374
  br label %if.end.20
if.else.20:
  store i1 %_371, ptr %_374
  br label %if.end.20
if.end.20:
  %_378 = load i1, ptr %_374
  %_379 = alloca i1
  br i1 %_378, label %if.then.21, label %if.else.21
if.then.21:
  %_380 = load ptr, ptr %step.1
  %_381 = load i32, ptr %x.1
  %_382 = getelementptr ptr, ptr %_380, i32 %_381
  %_383 = load ptr, ptr %_382
  %_384 = load i32, ptr %y.1
  %_385 = getelementptr i32, ptr %_383, i32 %_384
  %_386 = load i32, ptr %_385
  %_387 = sub i32 0, 1
  %_388 = icmp eq i32 %_386, %_387
  store i1 %_388, ptr %_379
  br label %if.end.21
if.else.21:
  store i1 %_378, ptr %_379
  br label %if.end.21
if.end.21:
  %_389 = load i1, ptr %_379
  br i1 %_389, label %if.then.22, label %if.end.22
if.then.22:
  %_390 = load i32, ptr %tail.1
  %_391 = load i32, ptr %tail.1
  %_392 = add i32 %_391, 1
  store i32 %_392, ptr %tail.1
  %_393 = load ptr, ptr %xlist.1
  %_394 = load i32, ptr %tail.1
  %_395 = getelementptr i32, ptr %_393, i32 %_394
  %_396 = load i32, ptr %_395
  %_397 = load i32, ptr %x.1
  store i32 %_397, ptr %_395
  %_398 = load ptr, ptr %ylist.1
  %_399 = load i32, ptr %tail.1
  %_400 = getelementptr i32, ptr %_398, i32 %_399
  %_401 = load i32, ptr %_400
  %_402 = load i32, ptr %y.1
  store i32 %_402, ptr %_400
  %_403 = load ptr, ptr %step.1
  %_404 = load i32, ptr %x.1
  %_405 = getelementptr ptr, ptr %_403, i32 %_404
  %_406 = load ptr, ptr %_405
  %_407 = load i32, ptr %y.1
  %_408 = getelementptr i32, ptr %_406, i32 %_407
  %_409 = load i32, ptr %_408
  %_410 = load i32, ptr %now.1
  %_411 = add i32 %_410, 1
  store i32 %_411, ptr %_408
  %_412 = load i32, ptr %x.1
  %_413 = load i32, ptr %targetx.1
  %_414 = icmp eq i32 %_412, %_413
  %_415 = alloca i1
  br i1 %_414, label %if.then.23, label %if.else.23
if.then.23:
  %_416 = load i32, ptr %y.1
  %_417 = load i32, ptr %targety.1
  %_418 = icmp eq i32 %_416, %_417
  store i1 %_418, ptr %_415
  br label %if.end.23
if.else.23:
  store i1 %_414, ptr %_415
  br label %if.end.23
if.end.23:
  %_419 = load i1, ptr %_415
  br i1 %_419, label %if.then.24, label %if.end.24
if.then.24:
  %_420 = load i32, ptr %ok.1
  store i32 1, ptr %ok.1
  br label %if.end.24
if.end.24:
  br label %if.end.22
if.end.22:
  %_421 = load i32, ptr %x.1
  %_422 = load ptr, ptr %xlist.1
  %_423 = load i32, ptr %head.1
  %_424 = getelementptr i32, ptr %_422, i32 %_423
  %_425 = load i32, ptr %_424
  %_426 = sub i32 %_425, 2
  store i32 %_426, ptr %x.1
  %_427 = load i32, ptr %y.1
  %_428 = load ptr, ptr %ylist.1
  %_429 = load i32, ptr %head.1
  %_430 = getelementptr i32, ptr %_428, i32 %_429
  %_431 = load i32, ptr %_430
  %_432 = add i32 %_431, 1
  store i32 %_432, ptr %y.1
  %_434 = load i32, ptr %x.1
  %_435 = load i32, ptr %N.1
  %_433 = call i1 @check(i32 %_434, i32 %_435)
  %_436 = alloca i1
  br i1 %_433, label %if.then.25, label %if.else.25
if.then.25:
  %_438 = load i32, ptr %y.1
  %_439 = load i32, ptr %N.1
  %_437 = call i1 @check(i32 %_438, i32 %_439)
  store i1 %_437, ptr %_436
  br label %if.end.25
if.else.25:
  store i1 %_433, ptr %_436
  br label %if.end.25
if.end.25:
  %_440 = load i1, ptr %_436
  %_441 = alloca i1
  br i1 %_440, label %if.then.26, label %if.else.26
if.then.26:
  %_442 = load ptr, ptr %step.1
  %_443 = load i32, ptr %x.1
  %_444 = getelementptr ptr, ptr %_442, i32 %_443
  %_445 = load ptr, ptr %_444
  %_446 = load i32, ptr %y.1
  %_447 = getelementptr i32, ptr %_445, i32 %_446
  %_448 = load i32, ptr %_447
  %_449 = sub i32 0, 1
  %_450 = icmp eq i32 %_448, %_449
  store i1 %_450, ptr %_441
  br label %if.end.26
if.else.26:
  store i1 %_440, ptr %_441
  br label %if.end.26
if.end.26:
  %_451 = load i1, ptr %_441
  br i1 %_451, label %if.then.27, label %if.end.27
if.then.27:
  %_452 = load i32, ptr %tail.1
  %_453 = load i32, ptr %tail.1
  %_454 = add i32 %_453, 1
  store i32 %_454, ptr %tail.1
  %_455 = load ptr, ptr %xlist.1
  %_456 = load i32, ptr %tail.1
  %_457 = getelementptr i32, ptr %_455, i32 %_456
  %_458 = load i32, ptr %_457
  %_459 = load i32, ptr %x.1
  store i32 %_459, ptr %_457
  %_460 = load ptr, ptr %ylist.1
  %_461 = load i32, ptr %tail.1
  %_462 = getelementptr i32, ptr %_460, i32 %_461
  %_463 = load i32, ptr %_462
  %_464 = load i32, ptr %y.1
  store i32 %_464, ptr %_462
  %_465 = load ptr, ptr %step.1
  %_466 = load i32, ptr %x.1
  %_467 = getelementptr ptr, ptr %_465, i32 %_466
  %_468 = load ptr, ptr %_467
  %_469 = load i32, ptr %y.1
  %_470 = getelementptr i32, ptr %_468, i32 %_469
  %_471 = load i32, ptr %_470
  %_472 = load i32, ptr %now.1
  %_473 = add i32 %_472, 1
  store i32 %_473, ptr %_470
  %_474 = load i32, ptr %x.1
  %_475 = load i32, ptr %targetx.1
  %_476 = icmp eq i32 %_474, %_475
  %_477 = alloca i1
  br i1 %_476, label %if.then.28, label %if.else.28
if.then.28:
  %_478 = load i32, ptr %y.1
  %_479 = load i32, ptr %targety.1
  %_480 = icmp eq i32 %_478, %_479
  store i1 %_480, ptr %_477
  br label %if.end.28
if.else.28:
  store i1 %_476, ptr %_477
  br label %if.end.28
if.end.28:
  %_481 = load i1, ptr %_477
  br i1 %_481, label %if.then.29, label %if.end.29
if.then.29:
  %_482 = load i32, ptr %ok.1
  store i32 1, ptr %ok.1
  br label %if.end.29
if.end.29:
  br label %if.end.27
if.end.27:
  %_483 = load i32, ptr %x.1
  %_484 = load ptr, ptr %xlist.1
  %_485 = load i32, ptr %head.1
  %_486 = getelementptr i32, ptr %_484, i32 %_485
  %_487 = load i32, ptr %_486
  %_488 = add i32 %_487, 2
  store i32 %_488, ptr %x.1
  %_489 = load i32, ptr %y.1
  %_490 = load ptr, ptr %ylist.1
  %_491 = load i32, ptr %head.1
  %_492 = getelementptr i32, ptr %_490, i32 %_491
  %_493 = load i32, ptr %_492
  %_494 = sub i32 %_493, 1
  store i32 %_494, ptr %y.1
  %_496 = load i32, ptr %x.1
  %_497 = load i32, ptr %N.1
  %_495 = call i1 @check(i32 %_496, i32 %_497)
  %_498 = alloca i1
  br i1 %_495, label %if.then.30, label %if.else.30
if.then.30:
  %_500 = load i32, ptr %y.1
  %_501 = load i32, ptr %N.1
  %_499 = call i1 @check(i32 %_500, i32 %_501)
  store i1 %_499, ptr %_498
  br label %if.end.30
if.else.30:
  store i1 %_495, ptr %_498
  br label %if.end.30
if.end.30:
  %_502 = load i1, ptr %_498
  %_503 = alloca i1
  br i1 %_502, label %if.then.31, label %if.else.31
if.then.31:
  %_504 = load ptr, ptr %step.1
  %_505 = load i32, ptr %x.1
  %_506 = getelementptr ptr, ptr %_504, i32 %_505
  %_507 = load ptr, ptr %_506
  %_508 = load i32, ptr %y.1
  %_509 = getelementptr i32, ptr %_507, i32 %_508
  %_510 = load i32, ptr %_509
  %_511 = sub i32 0, 1
  %_512 = icmp eq i32 %_510, %_511
  store i1 %_512, ptr %_503
  br label %if.end.31
if.else.31:
  store i1 %_502, ptr %_503
  br label %if.end.31
if.end.31:
  %_513 = load i1, ptr %_503
  br i1 %_513, label %if.then.32, label %if.end.32
if.then.32:
  %_514 = load i32, ptr %tail.1
  %_515 = load i32, ptr %tail.1
  %_516 = add i32 %_515, 1
  store i32 %_516, ptr %tail.1
  %_517 = load ptr, ptr %xlist.1
  %_518 = load i32, ptr %tail.1
  %_519 = getelementptr i32, ptr %_517, i32 %_518
  %_520 = load i32, ptr %_519
  %_521 = load i32, ptr %x.1
  store i32 %_521, ptr %_519
  %_522 = load ptr, ptr %ylist.1
  %_523 = load i32, ptr %tail.1
  %_524 = getelementptr i32, ptr %_522, i32 %_523
  %_525 = load i32, ptr %_524
  %_526 = load i32, ptr %y.1
  store i32 %_526, ptr %_524
  %_527 = load ptr, ptr %step.1
  %_528 = load i32, ptr %x.1
  %_529 = getelementptr ptr, ptr %_527, i32 %_528
  %_530 = load ptr, ptr %_529
  %_531 = load i32, ptr %y.1
  %_532 = getelementptr i32, ptr %_530, i32 %_531
  %_533 = load i32, ptr %_532
  %_534 = load i32, ptr %now.1
  %_535 = add i32 %_534, 1
  store i32 %_535, ptr %_532
  %_536 = load i32, ptr %x.1
  %_537 = load i32, ptr %targetx.1
  %_538 = icmp eq i32 %_536, %_537
  %_539 = alloca i1
  br i1 %_538, label %if.then.33, label %if.else.33
if.then.33:
  %_540 = load i32, ptr %y.1
  %_541 = load i32, ptr %targety.1
  %_542 = icmp eq i32 %_540, %_541
  store i1 %_542, ptr %_539
  br label %if.end.33
if.else.33:
  store i1 %_538, ptr %_539
  br label %if.end.33
if.end.33:
  %_543 = load i1, ptr %_539
  br i1 %_543, label %if.then.34, label %if.end.34
if.then.34:
  %_544 = load i32, ptr %ok.1
  store i32 1, ptr %ok.1
  br label %if.end.34
if.end.34:
  br label %if.end.32
if.end.32:
  %_545 = load i32, ptr %x.1
  %_546 = load ptr, ptr %xlist.1
  %_547 = load i32, ptr %head.1
  %_548 = getelementptr i32, ptr %_546, i32 %_547
  %_549 = load i32, ptr %_548
  %_550 = add i32 %_549, 2
  store i32 %_550, ptr %x.1
  %_551 = load i32, ptr %y.1
  %_552 = load ptr, ptr %ylist.1
  %_553 = load i32, ptr %head.1
  %_554 = getelementptr i32, ptr %_552, i32 %_553
  %_555 = load i32, ptr %_554
  %_556 = add i32 %_555, 1
  store i32 %_556, ptr %y.1
  %_558 = load i32, ptr %x.1
  %_559 = load i32, ptr %N.1
  %_557 = call i1 @check(i32 %_558, i32 %_559)
  %_560 = alloca i1
  br i1 %_557, label %if.then.35, label %if.else.35
if.then.35:
  %_562 = load i32, ptr %y.1
  %_563 = load i32, ptr %N.1
  %_561 = call i1 @check(i32 %_562, i32 %_563)
  store i1 %_561, ptr %_560
  br label %if.end.35
if.else.35:
  store i1 %_557, ptr %_560
  br label %if.end.35
if.end.35:
  %_564 = load i1, ptr %_560
  %_565 = alloca i1
  br i1 %_564, label %if.then.36, label %if.else.36
if.then.36:
  %_566 = load ptr, ptr %step.1
  %_567 = load i32, ptr %x.1
  %_568 = getelementptr ptr, ptr %_566, i32 %_567
  %_569 = load ptr, ptr %_568
  %_570 = load i32, ptr %y.1
  %_571 = getelementptr i32, ptr %_569, i32 %_570
  %_572 = load i32, ptr %_571
  %_573 = sub i32 0, 1
  %_574 = icmp eq i32 %_572, %_573
  store i1 %_574, ptr %_565
  br label %if.end.36
if.else.36:
  store i1 %_564, ptr %_565
  br label %if.end.36
if.end.36:
  %_575 = load i1, ptr %_565
  br i1 %_575, label %if.then.37, label %if.end.37
if.then.37:
  %_576 = load i32, ptr %tail.1
  %_577 = load i32, ptr %tail.1
  %_578 = add i32 %_577, 1
  store i32 %_578, ptr %tail.1
  %_579 = load ptr, ptr %xlist.1
  %_580 = load i32, ptr %tail.1
  %_581 = getelementptr i32, ptr %_579, i32 %_580
  %_582 = load i32, ptr %_581
  %_583 = load i32, ptr %x.1
  store i32 %_583, ptr %_581
  %_584 = load ptr, ptr %ylist.1
  %_585 = load i32, ptr %tail.1
  %_586 = getelementptr i32, ptr %_584, i32 %_585
  %_587 = load i32, ptr %_586
  %_588 = load i32, ptr %y.1
  store i32 %_588, ptr %_586
  %_589 = load ptr, ptr %step.1
  %_590 = load i32, ptr %x.1
  %_591 = getelementptr ptr, ptr %_589, i32 %_590
  %_592 = load ptr, ptr %_591
  %_593 = load i32, ptr %y.1
  %_594 = getelementptr i32, ptr %_592, i32 %_593
  %_595 = load i32, ptr %_594
  %_596 = load i32, ptr %now.1
  %_597 = add i32 %_596, 1
  store i32 %_597, ptr %_594
  %_598 = load i32, ptr %x.1
  %_599 = load i32, ptr %targetx.1
  %_600 = icmp eq i32 %_598, %_599
  %_601 = alloca i1
  br i1 %_600, label %if.then.38, label %if.else.38
if.then.38:
  %_602 = load i32, ptr %y.1
  %_603 = load i32, ptr %targety.1
  %_604 = icmp eq i32 %_602, %_603
  store i1 %_604, ptr %_601
  br label %if.end.38
if.else.38:
  store i1 %_600, ptr %_601
  br label %if.end.38
if.end.38:
  %_605 = load i1, ptr %_601
  br i1 %_605, label %if.then.39, label %if.end.39
if.then.39:
  %_606 = load i32, ptr %ok.1
  store i32 1, ptr %ok.1
  br label %if.end.39
if.end.39:
  br label %if.end.37
if.end.37:
  %_607 = load i32, ptr %ok.1
  %_608 = icmp eq i32 %_607, 1
  br i1 %_608, label %if.then.40, label %if.end.40
if.then.40:
  br label %while.end.0
  br label %if.end.40
if.end.40:
  %_609 = load i32, ptr %head.1
  %_610 = load i32, ptr %head.1
  %_611 = add i32 %_610, 1
  store i32 %_611, ptr %head.1
  br label %while.cond.0
while.end.0:
  %_612 = load i32, ptr %ok.1
  %_613 = icmp eq i32 %_612, 1
  br i1 %_613, label %if.then.41, label %if.else.41
if.then.41:
  %_615 = load ptr, ptr %step.1
  %_616 = load i32, ptr %targetx.1
  %_617 = getelementptr ptr, ptr %_615, i32 %_616
  %_618 = load ptr, ptr %_617
  %_619 = load i32, ptr %targety.1
  %_620 = getelementptr i32, ptr %_618, i32 %_619
  %_621 = load i32, ptr %_620
  %_614 = call ptr @toString(i32 %_621)
  call void @println(ptr %_614)
  br label %if.end.41
if.else.41:
  call void @print(ptr @string.0)
  br label %if.end.41
if.end.41:
  ret i32 0
}
define i1 @check(i32 %_a, i32 %_N) {
entry:
  %a.1 = alloca i32
  store i32 %_a, ptr %a.1
  %N.1 = alloca i32
  store i32 %_N, ptr %N.1
  %_0 = load i32, ptr %a.1
  %_1 = load i32, ptr %N.1
  %_2 = icmp slt i32 %_0, %_1
  %_3 = alloca i1
  br i1 %_2, label %if.then.0, label %if.else.0
if.then.0:
  %_4 = load i32, ptr %a.1
  %_5 = icmp sge i32 %_4, 0
  store i1 %_5, ptr %_3
  br label %if.end.0
if.else.0:
  store i1 %_2, ptr %_3
  br label %if.end.0
if.end.0:
  %_6 = load i1, ptr %_3
  ret i1 %_6
}

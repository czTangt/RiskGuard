package pokercc.android.expandablerecyclerview.sample.markets

import android.animation.ArgbEvaluator
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RoundRectShape
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cztang.riskguard.Domain.CommonDomain
import com.cztang.riskguard.UiUtil.dpToPx
import com.cztang.riskguard.databinding.ViewholderChildDeviceBinding
import com.cztang.riskguard.databinding.ViewholderParentDeviceBinding
import pokercc.android.expandablerecyclerview.ExpandableAdapter

class HookChildVH(val binding: ViewholderChildDeviceBinding) :
    ExpandableAdapter.ViewHolder(binding.root)

class HookParentVH(val binding: ViewholderParentDeviceBinding) :
    ExpandableAdapter.ViewHolder(binding.root)

class HookAdapter(
    private val hookData: List<CommonDomain>
) : ExpandableAdapter<ExpandableAdapter.ViewHolder>() {
    override fun onCreateGroupViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ExpandableAdapter.ViewHolder = LayoutInflater.from(viewGroup.context)
        .let { ViewholderParentDeviceBinding.inflate(it, viewGroup, false) }
        .let { HookParentVH(it) }

    override fun onCreateChildViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ExpandableAdapter.ViewHolder = LayoutInflater.from(viewGroup.context)
        .let { ViewholderChildDeviceBinding.inflate(it, viewGroup, false) }
        .let { HookChildVH(it) }

    override fun onBindChildViewHolder(
        holder: ExpandableAdapter.ViewHolder,
        groupPosition: Int,
        childPosition: Int,
        payloads: List<Any>
    ) {
        holder as HookChildVH
        holder.binding.childDevice.text =
            hookData[groupPosition].childData.getOrNull(childPosition)

        val childCount = getChildCount(groupPosition)
        val radius = 4.dpToPx()
        val shape = when {
            childCount == 1 -> {
                RoundRectShape(FloatArray(8) { radius }, null, null)
            }

            childPosition == 0 -> {
                RoundRectShape(
                    floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f),
                    null,
                    null
                )
            }

            childPosition == childCount - 1 -> {
                RoundRectShape(
                    floatArrayOf(0f, 0f, 0f, 0f, radius, radius, radius, radius),
                    null, null
                )
            }

            else -> {
                RoundRectShape(null, null, null)
            }
        }
        holder.binding.root.background = ShapeDrawable(shape).apply {
            paint.color = Color.WHITE
        }
    }

    override fun onBindGroupViewHolder(
        holder: ExpandableAdapter.ViewHolder,
        groupPosition: Int,
        expand: Boolean,
        payloads: List<Any>
    ) {
        holder as HookParentVH
        holder.binding.parentDevice.text = hookData[groupPosition].parentTitle
        if (payloads.isEmpty()) {
            val arrowImage = holder.binding.arrowDevice
            arrowImage.rotation = if (expand) -180f else 0f
            val circleDrawable = CircleDrawableHook()
            arrowImage.background = circleDrawable
            circleDrawable.progress = if (expand) 1f else 0f
        }
    }

    override fun onGroupViewHolderExpandChange(
        holder: ExpandableAdapter.ViewHolder,
        groupPosition: Int,
        animDuration: Long,
        expand: Boolean
    ) {
        holder as HookParentVH
        val arrowImage = holder.binding.arrowDevice
        arrowImage.animate()
            .setDuration(animDuration)
            .rotation(if (expand) -180f else 0f)
            .setUpdateListener {
                val progress = if (expand) it.animatedFraction else 1 - it.animatedFraction
                (arrowImage.background as CircleDrawableHook).progress = progress
            }
            .start()
    }

    override fun getGroupCount(): Int = hookData.size

    override fun getChildCount(groupPosition: Int): Int = hookData[groupPosition].childData.size

    fun expandAllGroups() {
        for (i in 0 until getGroupCount()) {
            expandGroup(i, true)
        }
    }

    fun collapseAllGroups() {
        for (i in 0 until getGroupCount()) {
            collapseGroup(i, true)
        }
    }
}

private class CircleDrawableHook : ShapeDrawable(OvalShape()) {
    private val argbEvaluator = ArgbEvaluator()
    private val startColor = 0xffEBF1FD.toInt() // light_blue
    private val endColor = 0xffEBF1FD.toInt() // light_blue
    var progress: Float = 0f
        set(value) {
            paint.color = argbEvaluator.evaluate(value, startColor, endColor) as Int
            invalidateSelf()
        }
}